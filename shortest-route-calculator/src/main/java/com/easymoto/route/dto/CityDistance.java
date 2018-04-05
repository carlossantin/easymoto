package com.easymoto.route.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 *  Data Transfer Object for City distances
 * 
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
@JsonRootName("CityDistance")
public class CityDistance {
  
  private Integer toCity;
  private Integer distance;

  protected CityDistance() { }

  public CityDistance(Integer toCity, Integer distance) {
    this.toCity = toCity;
    this.distance = distance;
  }

  public Integer getToCity() {
    return this.toCity;
  }

  public Integer getDistance() {
    return this.distance;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof CityDistance && ((CityDistance)o).getToCity().equals(this.toCity)) {
      return true;
    } else {
      return false;
    }
  }
}
