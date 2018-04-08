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
import com.easymoto.city.exception.MandatoryAttributeException;
import com.easymoto.city.exception.NonExistingCityException;
import com.easymoto.city.exception.EqualOriginDestinationCityException;
import static com.easymoto.city.util.ValuesUtil.getIntegerValueFromMap;
import static com.easymoto.city.util.ValuesUtil.getStringValueFromMap;

import java.util.Map;
import java.util.function.Function;
import java.util.Optional;
import java.util.NoSuchElementException;

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
   *            An integer value representing the city id.
   * @return The city if found.
   */
  @RequestMapping("/city/{id}")
  public City byId(@PathVariable("id") Integer id) {

    logger.info("city-service byId() invoked: " + id);
    Optional<City> city = Optional.ofNullable(cityRepository.findById(id));
    logger.info("city-service byId() found: " + city);

    return city.orElseThrow(() -> 
        new NonExistingCityException(
            String.format("City does not exist. Id: %s", id)));
  }

  /**
   * Get all recorded cities
   * 
   * @return A list of cities
   */
  @RequestMapping("/city/all")
  public Iterable<City> findAll() {

    logger.info("city-service findAll() invoked");
    Iterable<City> cities = cityRepository.findAll();

    return cities;
  }

  /**
   * Add a new city
   * 
   * @param payload
   *            A Json object having the fields id and name
   */
  @RequestMapping(
    value="/city/add", 
    method = RequestMethod.POST)
  public City addCity(@RequestBody Map<String, String> payload) {
    try {
      final Integer cityId = getIntegerValueFromMap(payload, "id");
      final String cityName = getStringValueFromMap(payload, "name");

      //Check if the city already exists
      Optional<City> city = Optional.ofNullable(cityRepository.findById(cityId));
      if (city.isPresent()) {
        throw new DuplicatedCityException(String.format("Duplicated city id: %s", cityId));
      }

      city = Optional.of(cityRepository.save(new City(cityId, cityName)));
      return city.get();
    } catch (NoSuchElementException ex) {
      throw new MandatoryAttributeException(String.format("{id: %s, name: %s}", payload.get("id"), payload.get("name")));
    }
  }

  @RequestMapping(
    value="/distance/add", 
    method = RequestMethod.POST)
  public void addDistance(@RequestBody Map<String, String> payload) {
    try {
      final Integer cityId = getIntegerValueFromMap(payload, "id");
      final Integer distance = getIntegerValueFromMap(payload, "distance");
      final Integer cityToId = getIntegerValueFromMap(payload, "to_id");

      //Check if origin city and destination city are diferent
      if (cityId.equals(cityToId)) {
        throw new EqualOriginDestinationCityException(String.format("The origin city and the destination city are the same {id: %s, to_id: %s}", cityId, cityToId));
      }

      //Check if the origin city exists
      Optional<City> cityFrom = Optional.ofNullable(cityRepository.findById(cityId));
      if (!cityFrom.isPresent()) {
        throw new NonExistingCityException(String.format("Origin city does not exist. Id: %s", cityId));
      }

      //Check if the destination city exists
      Optional<City> cityTo = Optional.ofNullable(cityRepository.findById(cityToId));
      if (!cityTo.isPresent()) {
        throw new NonExistingCityException(String.format("Destination city does not exist. Id: %s", cityToId));
      }

      cityFrom.get().addDistance(cityTo.get(), distance);
      cityFrom = Optional.of(cityRepository.save(cityFrom.get()));

      cityTo.get().addDistance(cityFrom.get(), distance);
      cityTo = Optional.of(cityRepository.save(cityTo.get()));
    } catch (NoSuchElementException ex) {
      throw new MandatoryAttributeException(String.format("{id: %s, distance: %s, to_id: %s}",
                    payload.get("id"), 
                    payload.get("distance"), 
                    payload.get("to_id")));
    }
  }

  /**
   * Update a city
   * 
   * @param payload
   *            A Json object having the fields id and name.
   */
  @RequestMapping(
    value="/city/update", 
    method = RequestMethod.POST)
  public City updateCity(@RequestBody Map<String, String> payload) {
    try {
      final Integer cityId = getIntegerValueFromMap(payload, "id");
      final String cityName = payload.get("name");

      //Check if the city exists
      Optional<City> city = Optional.ofNullable(cityRepository.findById(cityId));
      if (!city.isPresent()) {
        throw new NonExistingCityException(String.format("The city to be updated does not exist. Id: %s", cityId));
      }

      city.get().setName(cityName);

      city = Optional.of(cityRepository.save(city.get()));
      return city.get();
    } catch (NoSuchElementException ex) {
      throw new MandatoryAttributeException(String.format("{id: %s, name: %s}",
                    payload.get("id"), 
                    payload.get("name")));
    }
  }

  /**
   * Remove a city
   * 
   * @param id
   *            An integer value representing the city id.
   */
  @RequestMapping(
    value="/city/remove/{id}", 
    method = RequestMethod.POST)
  public void removeCity(@PathVariable("id") Integer id) {

    //Check if the city exists
    final Optional<City> city = Optional.ofNullable(cityRepository.findById(id));
    if (!city.isPresent()) {
      throw new NonExistingCityException(String.format("The city to be removed does not exist. Id: %s", id));
    }

    cityRepository.delete(city.get());
  }


}
