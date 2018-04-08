package com.easymoto.route.service;

import com.easymoto.route.dto.City;
import com.easymoto.route.dto.CityDistance;
import com.easymoto.route.processor.Graph;
import com.easymoto.route.processor.Node;
import com.easymoto.route.processor.ShortestRouteProcessor;
import com.easymoto.route.processor.ShortestRouteProcessorFactory;
import com.easymoto.route.exception.NonExistingCityException;

import java.util.logging.Logger;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

/**
 * Hide the access to the microservice inside this local service.
 * 
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
@Service
public class WebRouteService {

  @Autowired
  @LoadBalanced
  protected RestTemplate restTemplate;

  protected String serviceUrl;

  protected Logger logger = Logger.getLogger(WebRouteService.class
      .getName());

  public WebRouteService(String serviceUrl) {
    this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl
        : "http://" + serviceUrl;
  }

  /**
   * Load all cities recorded in Database
   * @return An array of cities 
   */
  private List<City> loadAllCities() {
    logger.info("loadAllCities() invoked");
    String completeServiceUrl = String.format("%s/city/all", serviceUrl);
    ResponseEntity<City[]> entity = restTemplate.getForEntity(completeServiceUrl, City[].class);
    return Collections.unmodifiableList(Arrays.asList(entity.getBody()));
  }

  private City findById(Integer id) {
    logger.info("findById() invoked: for " + id);
    return restTemplate.getForObject(serviceUrl + "/city/{number}",
        City.class, id);
  }

  public List<Map<String, String>> calculateShortestRoute(Integer origin, Integer destination) {
    Optional<City> cityOrigin = Optional.ofNullable(findById(origin));
    Optional<City> cityDestination = Optional.ofNullable(findById(destination));

    if (!cityOrigin.isPresent() || !cityDestination.isPresent()) {
      String msg = String.format("Non existing city detected. Origin: %s, Destination: %s", cityOrigin, cityDestination);
      throw new NonExistingCityException(msg);
    }

    ShortestRouteProcessor routeProcessor = ShortestRouteProcessorFactory.getShortestRouteProcessor();
    final List<City> allCities = loadAllCities();
    final Graph graph = routeProcessor.calculateShortestPathFromSource(allCities, origin);

    return formatGraphToReturn(graph, origin, destination);

  }

  private List<Map<String, String>> formatGraphToReturn(Graph graph, Integer origin, Integer destination) {
    final List<Map<String, String>> result = new ArrayList();
    
    Stream<Node> streamNodeDestination = graph.getNodes().stream().filter(n ->
      n.getId().equals(destination));

    streamNodeDestination.forEach(nodeDestination -> {
      final TotalDistance totalDistance = new TotalDistance();
      nodeDestination.getShortestPath().forEach(nodeOnPath -> {
        Map data = new HashMap();
        if (nodeOnPath.getId().equals(origin)) {
          data.put("From", nodeOnPath.getName());
        } else {
          data.put("To", nodeOnPath.getName());
        }
        Optional<Node> lastVisitedNode = totalDistance.getLastVisitedNode();
        if (lastVisitedNode.isPresent()) {
          totalDistance.addDistance(lastVisitedNode.get().getAdjacentNodes().get(nodeOnPath));
        }
        totalDistance.setLastVisitedNode(nodeOnPath);
        result.add(data);
      });

      totalDistance.addDistance(totalDistance.getLastVisitedNode()
                                    .get().getAdjacentNodes()
                                    .get(nodeDestination));
      Map data = new HashMap();
      data.put("To", nodeDestination.getName());
      result.add(data);
      data = new HashMap();
      data.put("Total", String.valueOf(totalDistance.getDistance()));
      result.add(data);
    });
    return result;
  }

  private class TotalDistance {
    private Integer distance = 0;
    private Optional<Node> lastVisitedNode = Optional.empty();

    public void addDistance(Integer distance) {
      this.distance += distance;
    }

    public Integer getDistance() {
      return this.distance;
    }

    public void setLastVisitedNode(Node n) {
      this.lastVisitedNode = Optional.ofNullable(n);
    }

    public Optional<Node> getLastVisitedNode() {
      return this.lastVisitedNode;
    }
  }

}
