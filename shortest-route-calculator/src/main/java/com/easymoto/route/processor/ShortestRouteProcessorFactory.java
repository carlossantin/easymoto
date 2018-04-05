package com.easymoto.route.processor;

import com.easymoto.dijkstra.dijkstra.Dijkstra;

public class ShortestRouteProcessorFactory {

  public static ShortestRouteProcessor getShortestRouteProcessor() {
    return new Dijkstra();
  }

}
