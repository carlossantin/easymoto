package com.easymoto.city.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.easymoto.city.CityRepository;
import com.easymoto.city.controller.CityConfiguration;

@EnableAutoConfiguration
@EnableDiscoveryClient
@SpringBootApplication
@Import(CityConfiguration.class)
public class CityServer {

    @Autowired
    protected CityRepository cityRepository;

    protected Logger logger = Logger.getLogger(CityServer.class.getName());

    public static void main(String[] args) {
        // Will configure using city-server.yml
        System.setProperty("spring.config.name", "city-server");

        SpringApplication.run(CityServer.class, args);
    }
}
