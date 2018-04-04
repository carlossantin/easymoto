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

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "city_distance", joinColumns = @JoinColumn(name = "from_id"))
  @MapKeyColumn(name="to_id")
  @Column(name = "distance")
  private Map<Integer, Integer> distances = new HashMap<Integer, Integer>();

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

  public Integer getDistance(Integer toCityId) {
    return distances.get(toCityId);
  }

  public void addDistance(Integer toCityId, Integer distance) {
    distances.put(toCityId, distance);
  }

  public void removeDistance(Integer toCityId) {
    distances.remove(toCityId);
  }

  public Map<Integer, Integer> getDistances() {
    return this.distances;
  }



  @Override
  public String toString() {
   return name + " [" + id + "]";
  }
}
