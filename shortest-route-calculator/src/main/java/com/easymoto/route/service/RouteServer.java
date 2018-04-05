package com.easymoto.route.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import com.easymoto.route.controller.RouteController;

@ComponentScan(useDefaultFilters = false) // Disable component scanner
@EnableDiscoveryClient
@SpringBootApplication
public class RouteServer {

  /**
   * URL uses the logical name of account-service - upper or lower case,
   * doesn't matter.
   */
  public static final String CITY_SERVICE_URL = "http://CITY-SERVICE";

  public static void main(String[] args) {
      // Will configure using route-server.yml
      System.setProperty("spring.config.name", "route-server");

      SpringApplication.run(RouteServer.class, args);
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
  public WebRouteService routeService() {
    return new WebRouteService(CITY_SERVICE_URL);
  }

  /**
   * Create the controller, passing it the {@link WebCityService} to use.
   * 
   * @return
   */
  @Bean
  public RouteController routeController() {
    return new RouteController(routeService());
  }
}
