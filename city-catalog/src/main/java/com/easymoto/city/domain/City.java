package com.easymoto.city.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import java.util.Map;
import java.util.HashMap;
import javax.persistence.ElementCollection;
import javax.persistence.CollectionTable;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.FetchType;
import javax.persistence.Transient;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Persistent city entity with JPA markup. Cities are stored in an H2
 * relational database.
 * 
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
@Entity
@Table(name = "city")
public class City implements Serializable {
  
  private static final long serialVersionUID = 1L;

  @Id
  private Integer id;

  @Column(name = "name")
  private String name;

  // TODO: Use a Map instead of a List for city distances. 
  // It was used a list because of problems on converting the json created to this map to a Java object
  // on restTemplate methods.

  /*@ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "city_distance", joinColumns = @JoinColumn(name = "from_id"))
  @MapKeyColumn(name="to_id")
  @Column(name = "distance")
  private Map<City, Integer> distances = new HashMap<City, Integer>();*/

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "city_distance", joinColumns = @JoinColumn(name = "from_id"))
  List<CityDistance> distances = new ArrayList();

  public City(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  protected City() {
  }

  public String getName() {
    return this.name;
  }
  
  public Integer getId() {
    return this.id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getDistance(City toCityId) {
    int idx = distances.indexOf(new CityDistance(toCityId.getId(), -1));
    Optional<CityDistance> cityDistance = Optional.empty();
    if (idx > -1) {
      cityDistance = Optional.of(distances.get(idx));
    }

    Optional<Integer> distance = Optional.empty();
    if (cityDistance.isPresent()) {
      distance = Optional.of(cityDistance.get().getDistance());
    }

    return distance.get();
  }

  public void addDistance(City toCityId, Integer distance) {
    this.removeDistance(toCityId);
    distances.add(new CityDistance(toCityId.getId(), distance));
  }

  public void removeDistance(City toCityId) {
    int idx = distances.indexOf(new CityDistance(toCityId.getId(), -1));
    if (idx > -1) {
      distances.remove(idx);
    }
  }

  public List<CityDistance> getDistances() {
    return this.distances;
  }

  @Override
  public String toString() {
    return String.format("%s [%s]", name, id);
  }

  @Override
  public boolean equals(Object o) {
    if(o instanceof City && ((City)o).getId().equals(this.id)) {
        return true;
    } else {
        return false;
    }
  }

  @Override
  public int hashCode(){
    return 37 * this.id;
  }
}
