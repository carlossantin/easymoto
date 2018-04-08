package com.easymoto.city.domain;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Column;

@Embeddable
public class CityDistance implements Serializable {
  
  @Column(name = "to_id")
  private Integer toCity;
  @Column(name = "distance")
  private Integer distance;

  public CityDistance() { }

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
