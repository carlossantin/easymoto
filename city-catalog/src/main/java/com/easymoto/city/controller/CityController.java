package com.easymoto.city.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easymoto.city.domain.City;
import com.easymoto.city.CityRepository;
import com.easymoto.city.exception.DuplicatedCityException;

import java.util.Map;
import java.util.function.Function;


/**
 * A RESTFul controller for accessing city information.
 * 
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
@RestController
public class CityController {

  protected Logger logger = Logger.getLogger(CityController.class
      .getName());
  protected CityRepository cityRepository;

  /**
   * Create an instance plugging in the respository of Cities.
   * 
   * @param cityRepository
   *            A city repository implementation.
   */
  @Autowired
  public CityController(CityRepository cityRepository) {
    this.cityRepository = cityRepository;

    logger.info("CityRepository says system has "
        + cityRepository.countCities() + " cities");
  }

  /**
   * Fetch a city with the specified id.
   * 
   * @param id
   *            An integer value.
   * @return The city if found.
   */
  @RequestMapping("/city/{id}")
  public City byId(@PathVariable("id") String id) {

    logger.info("city-service byId() invoked: " + id);
    City city = cityRepository.findById(Integer.valueOf(id));
    logger.info("city-service byId() found: " + city);

    return city;
  }

  /**
   * Add a new city
   * 
   * @param city
   *            A Json object having the fields: id, name, distance, toId.
   */
  @RequestMapping(
    value="/add-city", 
    method = RequestMethod.POST)
  public void addCity(@RequestBody Map<String, String> payload) {
    final Integer cityId = Integer.valueOf(payload.get("id"));
    final String cityName = payload.get("name");
    final Integer distance = Integer.valueOf(payload.get("distance"));
    final Integer cityToId = Integer.valueOf(payload.get("to_id"));

    //Check if the city already exists
    final City city = cityRepository.findById(cityId);
    if (city != null) {
      throw new DuplicatedCityException("Duplicated city id: " + cityId);
    }

    cityRepository.save(new City(cityId, cityName));
  }


}