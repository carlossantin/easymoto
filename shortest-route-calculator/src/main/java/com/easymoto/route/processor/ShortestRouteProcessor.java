package com.easymoto.route.processor;

import com.easymoto.route.dto.City;
import java.util.List;

public interface ShortestRouteProcessor {

  public Graph calculateShortestPathFromSource(List<City> allCities, Integer idOriginCity);

}
