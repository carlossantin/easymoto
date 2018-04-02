package com.easymoto.facade.service;

import com.easymoto.facade.dto.City;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Hide the access to the microservice inside this local service.
 * 
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
@Service
public class WebCityService {

  @Autowired
  @LoadBalanced
  protected RestTemplate restTemplate;

  protected String serviceUrl;

  protected Logger logger = Logger.getLogger(WebCityService.class
      .getName());

  public WebCityService(String serviceUrl) {
    this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl
        : "http://" + serviceUrl;
  }

  public City findById(Integer id) {
    logger.info("findById() invoked: for " + id);
    return restTemplate.getForObject(serviceUrl + "/city/{number}",
        City.class, id);
  }

}
