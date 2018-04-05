package com.easymoto.facade.service;

import java.util.List;
import java.util.logging.Logger;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus; 

/**
 * Hide the access to the microservice inside this local service.
 * 
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
@Service
public class WebRouteService {

  @Autowired
  @LoadBalanced
  protected RestTemplate restTemplate;

  protected String serviceUrl;

  protected Logger logger = Logger.getLogger(WebRouteService.class
      .getName());

  public WebRouteService(String serviceUrl) {
    this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl
        : "http://" + serviceUrl;
  }

  public List<Map<String, String>> calculateShortestRoute(Integer origin, Integer destination) {
    logger.info("calculateShortestRoute() invoked: from " + origin + " to " + destination);
    return restTemplate.getForObject(serviceUrl + "/router/city/shortest/{origin}/to/{destination}",
        List.class, origin, destination);
  }

  /** 
   * Check if all services are up 
   */ 
  public HttpStatus checkHealth() { 
    //TODO: Check if the database is OK 
    try { 
      ResponseEntity<Object> entity = restTemplate.getForEntity(serviceUrl + "/health.json", Object.class); 
      return entity.getStatusCode(); 
    } catch (Exception e) { 
      return HttpStatus.INTERNAL_SERVER_ERROR; 
    } 
  }

}
