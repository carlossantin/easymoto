package com.easymoto.facade.controller;

import com.easymoto.facade.dto.City;
import com.easymoto.facade.service.WebCityService;
import com.easymoto.facade.service.WebRouteService;
import static com.easymoto.facade.util.ValuesUtil.getIntegerValueFromMap;
import static com.easymoto.facade.util.ValuesUtil.getStringValueFromMap;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus; 

/**
 * Client controller, fetches data info from the microservice 
 * 
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
@RestController
public class WebController {

  @Autowired
  protected WebCityService cityService;

  @Autowired
  protected WebRouteService routeService;

  protected Logger logger = Logger.getLogger(WebController.class
      .getName());

  public WebController(WebCityService cityService, WebRouteService routeService) {
    this.cityService = cityService;
    this.routeService = routeService;
  }

  @RequestMapping("/router/city/{id}")
  public City byId(@PathVariable("id") Integer id) {

    logger.info("web-service byId() invoked: " + id);

    City city = cityService.findById(id);
    logger.info("web-service byId() found: " + city);
    return city;
  }

  /**
   * Manage cities
   * 
   * @param payload
   *            A Json object having the fields id, name, operation, distance, and to_id
   */
  @RequestMapping(
    value="/router/city", 
    method = RequestMethod.POST)
  public City manageCity(@RequestBody Map<String, String> payload) {
    final Integer cityId = getIntegerValueFromMap(payload, "id");
    final String operation = getStringValueFromMap(payload, "Operation");
 
    City cityResponse = null;

    switch(operation) {
      case "ADD":
        cityResponse = cityService.addCity(payload);
        break;
      case "DEL":
        cityService.removeCity(cityId);
        break;
      case "UPT":
        cityResponse = cityService.updateCity(payload);
        break;
    }

    return cityResponse;
  }

  /**
   * Calculate the shortest route between the origin and the destination
   * @param origin The origin city id
   * @param destination The destination city id
   * 
   */
  @RequestMapping(
    value="/router/city/shortest/{origin}/to/{destination}", 
    method = RequestMethod.GET)
  public List<Map<String, String>> calculateShortestRoute(@PathVariable("origin") Integer origin, 
                      @PathVariable("destination") Integer destination) {
    logger.info("web-service calculateShortestRoute() from: " + origin + " to " + destination);
    return routeService.calculateShortestRoute(origin, destination);
  }

  @RequestMapping( 
    value="/router/health",  
    method = RequestMethod.GET) 
  public Integer checkHealth() { 

    return (cityService.checkHealth() == routeService.checkHealth()) && (routeService.checkHealth() == HttpStatus.OK) ?
              HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value(); 
  }

}
