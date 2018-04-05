package com.easymoto.facade.controller;

import com.easymoto.facade.dto.City;
import com.easymoto.facade.service.WebCityService;
import static com.easymoto.facade.util.ValuesUtil.getIntegerValueFromMap;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Client controller, fetches City info from the microservice via
 * {@link WebCityService}.
 * 
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
@RestController
public class WebCityController {

  @Autowired
  protected WebCityService cityService;

  protected Logger logger = Logger.getLogger(WebCityController.class
      .getName());

  public WebCityController(WebCityService cityService) {
    this.cityService = cityService;
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
    final String cityName = payload.get("name");
    final String operation = payload.get("Operation");
    final Integer distance = getIntegerValueFromMap(payload, "distance");
    final Integer cityToId = getIntegerValueFromMap(payload, "to_id");

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

    // //Check mandatory attributes
    // if (cityId == null || cityName == null) {
    //   throw new MandatoryAttributeException(String.format("{id: %s, name: %s}", cityId, cityName));
    // }

    //Check if the city already exists
    // City city = cityRepository.findById(cityId);
    // if (city != null) {
    //   throw new DuplicatedCityException(String.format("Duplicated city id: %s", cityId));
    // }

    return cityResponse;
  }

  @RequestMapping( 
    value="/router/health",  
    method = RequestMethod.GET) 
  public Integer checkHealth() { 
    return cityService.checkHealth().value(); 
  }

}
