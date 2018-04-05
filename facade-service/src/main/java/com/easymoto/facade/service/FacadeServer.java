package com.easymoto.facade.service;

import com.easymoto.facade.controller.WebController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;


/**
 * Facade web-server. Works as a microservice client, fetching data from the
 * City-Service. Uses the Discovery Server (Eureka) to find the microservice.
 * 
 * @author Carlos E. Santin <cesantin@gmail.com>
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(useDefaultFilters = false) // Disable component scanner
public class FacadeServer {

  /**
   * URL uses the logical name of account-service - upper or lower case,
   * doesn't matter.
   */
  public static final String CITY_SERVICE_URL = "http://CITY-SERVICE";
  public static final String ROUTE_SERVICE_URL = "http://ROUTE-SERVICE";

  /**
   * Run the application using Spring Boot and an embedded servlet engine.
   * 
   * @param args
   *            Program arguments - ignored.
   */
  public static void main(String[] args) {
    // Tell server to look for facade-server.properties or facade-server.yml
    System.setProperty("spring.config.name", "facade-server");
    SpringApplication.run(FacadeServer.class, args);
  }

  /**
   * A customized RestTemplate that has the ribbon load balancer build in.
   * Note that prior to the "Brixton" 
   * 
   * @return
   */
  @LoadBalanced
  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

  /**
   * The CityService encapsulates the interaction with the micro-service.
   * 
   * @return A new service instance.
   */
  @Bean
  public WebCityService cityService() {
    return new WebCityService(CITY_SERVICE_URL);
  }

  /**
   * Create the controller, passing it the {@link WebCityService} to use.
   * 
   * @return
   */
  @Bean
  public WebController cityController() {
    return new WebController(cityService(), routeService());
  }

  /**
   * The RouteService encapsulates the interaction with the micro-service.
   * 
   * @return A new service instance.
   */
  @Bean
  public WebRouteService routeService() {
    return new WebRouteService(ROUTE_SERVICE_URL);
  }

}
