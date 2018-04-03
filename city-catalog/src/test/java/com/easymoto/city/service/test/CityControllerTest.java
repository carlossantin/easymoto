package com.easymoto.city.service.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.easymoto.city.domain.City;
import com.easymoto.city.controller.CityController;
import com.easymoto.city.exception.DuplicatedCityException;
import com.easymoto.city.exception.MandatoryAttributeException;
import com.easymoto.city.exception.NonExistingCityException;
import com.easymoto.city.exception.EqualOriginDestinationCityException;
import java.util.Map;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CityControllerTest {

  @Autowired
  private CityController controller;

  @Test
  public void contextLoads() throws Exception {
    assertThat(controller).isNotNull();
  }

  @Test(expected = DuplicatedCityException.class)
  public void testAddingTwiceTheSameCity() throws Exception {
    Map<String, String> payload = new HashMap();
    payload.put("id", "-1");
    payload.put("name", "duplicated");
    controller.addCity(payload);
    controller.addCity(payload);
  }

  @Test(expected = MandatoryAttributeException.class)
  public void testAddingCityWithoutId() throws Exception {
    Map<String, String> payload = new HashMap();
    payload.put("name", "duplicated");
    controller.addCity(payload);
  }

  @Test(expected = MandatoryAttributeException.class)
  public void testAddingCityWithoutName() throws Exception {
    Map<String, String> payload = new HashMap();
    payload.put("id", "-1");
    controller.addCity(payload);
  }

  @Test(expected = MandatoryAttributeException.class)
  public void testAddingDistanceWithoutOriginCity() throws Exception {
    Map<String, String> payload = new HashMap();
    payload.put("to_id", "-2");
    payload.put("distance", "30");
    controller.addDistance(payload);
  }

  @Test(expected = MandatoryAttributeException.class)
  public void testAddingDistanceWithoutDestinationCity() throws Exception {
    Map<String, String> payload = new HashMap();
    payload.put("id", "-1");
    payload.put("distance", "30");
    controller.addDistance(payload);
  }

  @Test(expected = MandatoryAttributeException.class)
  public void testAddingDistanceWithoutDistance() throws Exception {
    Map<String, String> payload = new HashMap();
    payload.put("id", "-1");
    payload.put("to_id", "-2");
    controller.addDistance(payload);
  }

  @Test(expected = EqualOriginDestinationCityException.class)
  public void testAddingEqualOriginAndDestination() throws Exception {
    Map<String, String> payload = new HashMap();
    payload.put("id", "-1");
    payload.put("to_id", "-1");
    payload.put("distance", "30");
    controller.addDistance(payload);
  }

  @Test(expected = NonExistingCityException.class)
  public void testAddingNonExistingDestinationCity() throws Exception {
    Map<String, String> payload = new HashMap();
    payload.put("id", "-1");
    payload.put("name", "testCity");
    controller.addCity(payload);
    payload.put("to_id", "-2");
    payload.put("distance", "30");
    controller.addDistance(payload);
  }

  @Test(expected = NonExistingCityException.class)
  public void testAddingNonExistingOriginCity() throws Exception {
    Map<String, String> payload = new HashMap();
    payload.put("id", "-2");
    payload.put("name", "testCity");
    controller.addCity(payload);
    payload.put("id", "-3");
    payload.put("to_id", "-2");
    payload.put("distance", "30");
    controller.addDistance(payload);
  }

  @Test
  public void testAddingCity() throws Exception {
    Map<String, String> payload = new HashMap();
    payload.put("id", "-4");
    payload.put("name", "testCity");
    City city = controller.addCity(payload);
    assertEquals(Integer.valueOf(-4), (Integer)city.getId());
    assertEquals("testCity", city.getName());
  }

  @Test
  public void testAddingDistance() throws Exception {
    Map<String, String> payload = new HashMap();
    payload.put("id", "-5");
    payload.put("name", "testCityOrigin");
    controller.addCity(payload);
    
    payload.put("id", "-6");
    payload.put("name", "testCityDestination");
    controller.addCity(payload);

    payload.put("id", "-5");
    payload.put("distance", "30");
    payload.put("to_id", "-6");
    controller.addDistance(payload);

    City cityOrigin = controller.byId(-5);
    City cityDestination = controller.byId(-6);

    assertEquals(Integer.valueOf(30), (Integer)cityOrigin.getDistance(-6));
    assertEquals(Integer.valueOf(30), (Integer)cityDestination.getDistance(-5));
  }

  @Test(expected = NonExistingCityException.class)
  public void testUpdateNonExistingCity() throws Exception {
    Map<String, String> payload = new HashMap();
    payload.put("id", "-1000");
    payload.put("name", "cityNotExistent");
    controller.updateCity(payload);
  }

  @Test
  public void testUpdateCity() throws Exception {
    Map<String, String> payload = new HashMap();
    payload.put("id", "-7");
    payload.put("name", "oldCityName");
    controller.addCity(payload);

    City city = controller.byId(-7);
    assertEquals("oldCityName", city.getName());

    payload.put("name", "newCityName");
    controller.updateCity(payload);

    city = controller.byId(-7);
    assertEquals("newCityName", city.getName());
  }
}
