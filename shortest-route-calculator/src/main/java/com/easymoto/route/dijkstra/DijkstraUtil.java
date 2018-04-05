package com.easymoto.dijkstra.dijkstra;

import com.easymoto.route.dto.City;
import com.easymoto.route.dto.CityDistance;
import com.easymoto.route.processor.Node;
import com.easymoto.route.processor.Graph;

import java.util.Map;
import java.util.HashMap;

/**
 * Helper methods for dijkstra algorithm
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
public class DijkstraUtil {

  public static Graph createGraph(City[] cities) {
    final Map<Integer, Node> mapNodes = new HashMap();
    
    for (final City city: cities) {      
      Node node = mapNodes.get(city.getId());
      if (node == null) {
        node = new Node(city.getId(), city.getName());
        mapNodes.put(node.getId(), node);
      }
      node.setName(city.getName());

      for (final CityDistance cityDistance: city.getDistances()) {
        Node nodeDestination = mapNodes.get(cityDistance.getToCity());
        if (nodeDestination == null) {
          nodeDestination = new Node(cityDistance.getToCity(), "");
          mapNodes.put(nodeDestination.getId(), nodeDestination);
        }
        node.addDestination(nodeDestination, cityDistance.getDistance());
      }

    }

    final Graph graph = new Graph();
    for (Node node: mapNodes.values()) {
      graph.addNode(node);
    }

    return graph;
  }

}  
