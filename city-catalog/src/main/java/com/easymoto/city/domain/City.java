package com.easymoto.city.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

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

  public City(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  protected City() {
  }

  @Id
  private Integer id;

  @Column(name = "name")
  private String name;
  
  public String getName() {
    return this.name;
  }
  
  public Integer getId() {
    return this.id;
  }

  @Override
  public String toString() {
   return name + " [" + id + "]";
  }
}
