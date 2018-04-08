package com.easymoto.dijkstra.dijkstra;

import com.easymoto.route.dto.City;
import com.easymoto.route.dto.CityDistance;
import com.easymoto.route.processor.Node;
import com.easymoto.route.processor.Graph;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

/**
 * Helper methods for dijkstra algorithm
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
public class DijkstraUtil {

  public static Graph createGraph(List<City> cities) {
    final Map<Integer, Node> mapNodes = new HashMap();
    
    cities.forEach((City city) -> {
      Optional<Node> optionalNode = Optional.ofNullable(mapNodes.get(city.getId()));
      if (!optionalNode.isPresent()) {
        optionalNode = Optional.of(new Node(city.getId(), city.getName()));
        mapNodes.put(optionalNode.get().getId(), optionalNode.get());
      }
      final Node node = optionalNode.get();
      node.setName(city.getName());

      city.getDistances().forEach((CityDistance cityDistance) -> {
        Optional<Node> nodeDestination = Optional.ofNullable(mapNodes.get(cityDistance.getToCity()));
        if (!nodeDestination.isPresent()) {
          nodeDestination = Optional.of(new Node(cityDistance.getToCity(), ""));
          mapNodes.put(nodeDestination.get().getId(), nodeDestination.get());
        }
        node.addDestination(nodeDestination.get(), cityDistance.getDistance());
      });

    });

    final Graph graph = new Graph();
    mapNodes.values().forEach((Node n) -> {
      graph.addNode(n);
    });

    return graph;
  }

}  
