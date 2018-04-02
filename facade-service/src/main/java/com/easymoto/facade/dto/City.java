package com.easymoto.facade.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Persistent city entity with JPA markup. Cities are stored in an H2
 * relational database.
 * 
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
@JsonRootName("City")
public class City {
  
  protected Integer id;
  protected String name;

  protected City() {
  }
  
  public String getName() {
    return this.name;
  }

  protected void setName(String name) {
    this.name = name;
  }
  
  public Integer getId() {
    return this.id;
  }

  protected void setId(Integer id) {
    this.id = id;
  }

  @Override
  public String toString() {
   return name + " [" + id + "]";
  }
}
