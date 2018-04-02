package com.easymoto.facade.controller;

import com.easymoto.facade.dto.City;
import com.easymoto.facade.service.WebCityService;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
