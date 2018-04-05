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
  private City[] loadAllCities() {
    logger.info("loadAllCities() invoked");
    String completeServiceUrl = String.format("%s/city/all", serviceUrl);
    ResponseEntity<City[]> entity = restTemplate.getForEntity(completeServiceUrl, City[].class);
    return entity.getBody();
  }

  private City findById(Integer id) {
    logger.info("findById() invoked: for " + id);
    return restTemplate.getForObject(serviceUrl + "/city/{number}",
        City.class, id);
  }

  public List<Map<String, String>> calculateShortestRoute(Integer origin, Integer destination) {
    City cityOrigin = findById(origin);
    City cityDestination = findById(destination);

    if (cityOrigin == null || cityDestination == null) {
      String msg = String.format("Non existing city detected. Origin: %s, Destination: %s", cityOrigin, cityDestination);
      throw new NonExistingCityException(msg);
    }

    ShortestRouteProcessor routeProcessor = ShortestRouteProcessorFactory.getShortestRouteProcessor();
    final City[] allCities = loadAllCities();
    final Graph graph = routeProcessor.calculateShortestPathFromSource(allCities, origin);

    return formatGraphToReturn(graph, origin, destination);

  }

  private List<Map<String, String>> formatGraphToReturn(Graph graph, Integer origin, Integer destination) {
    List<Map<String, String>> result = new ArrayList();
    
    for (Node n: graph.getNodes()) {
      if (n.getId().equals(destination)) {
        Integer totalDistance = 0;
        Node lastVisitedNode = null;
        
        for (Node nodeOnPath: n.getShortestPath()) {
          Map data = new HashMap();
          if (nodeOnPath.getId().equals(origin)) {
            data.put("From", nodeOnPath.getName());
          } else {
            data.put("To", nodeOnPath.getName());
          }
          if (lastVisitedNode != null) {
            totalDistance += lastVisitedNode.getAdjacentNodes().get(nodeOnPath);
          }
          lastVisitedNode = nodeOnPath;
          result.add(data);
        }

        totalDistance += lastVisitedNode.getAdjacentNodes().get(n);
        Map data = new HashMap();
        data.put("To", n.getName());
        result.add(data);
        data = new HashMap();
        data.put("Total", String.valueOf(totalDistance));
        result.add(data);
        break;
      }
    }

    return result;
  }

}
