package com.easymoto.city;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.easymoto.city.domain.City;

/**
 * Repository for City data implemented using Spring Data JPA.
 * 
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
public interface CityRepository extends CrudRepository<City, Integer> {
  /**
   * Find an city with the specified id.
   *
   * @param id
   * @return The city if found, null otherwise.
   */
  public City findById(Integer id);

  public City save(City city);

  /**
   * Fetch the number of cities known to the system.
   * 
   * @return The number of cities.
   */
  @Query("SELECT count(*) from City")
  public int countCities();
}
