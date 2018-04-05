package com.easymoto.route.processor;

import com.easymoto.route.dto.City;

public interface ShortestRouteProcessor {

  public Graph calculateShortestPathFromSource(City[] allCities, Integer idOriginCity);

}
