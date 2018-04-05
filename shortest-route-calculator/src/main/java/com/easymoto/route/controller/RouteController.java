package com.easymoto.route.controller;

import com.easymoto.route.service.WebRouteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * A RESTFul controller for accessing route information.
 * 
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
@RestController
public class RouteController {

  @Autowired
  protected WebRouteService routeService;

  @Autowired
  public RouteController(WebRouteService routeService) {
    this.routeService = routeService;
  }

  /**
   * Calculate the shortest route between the origin and the destination
   * 
   */
  @RequestMapping(
    value="/router/city/shortest/{origin}/to/{destination}", 
    method = RequestMethod.GET)
  public List<Map<String, String>> calculateShortestRoute(@PathVariable("origin") Integer origin, 
                      @PathVariable("destination") Integer destination) {
    return routeService.calculateShortestRoute(origin, destination);
  }
}
