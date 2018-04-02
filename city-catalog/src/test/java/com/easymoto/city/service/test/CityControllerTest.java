package com.easymoto.city.service.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.easymoto.city.controller.CityController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CityControllerTest {

  @Autowired
  private CityController controller;

  @Test
  public void contextLoads() throws Exception {
    assertThat(controller).isNotNull();
  }

}
