package com.easymoto.city;

import java.util.logging.Logger;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Random;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;

@Configuration
@ComponentScan
@EntityScan("com.easymoto.city")
@EnableJpaRepositories("com.easymoto.city")
@PropertySource("classpath:db-config.properties")
public class CityConfiguration {

  protected Logger logger;

  public CityConfiguration() {
    logger = Logger.getLogger(getClass().getName());
  }

  /**
   * Creates an in-memory database populated with test data for fast
   * testing
   */
  @Bean
  public DataSource dataSource() {
    logger.info("dataSource() invoked");

    // Create an in-memory H2 relational database containing some demo
    // cities.
    DataSource dataSource = (new EmbeddedDatabaseBuilder()).addScript("classpath:testdb/schema.sql")
        .addScript("classpath:testdb/data.sql").build();

    logger.info("dataSource = " + dataSource);

    // Sanity check
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    List<Map<String, Object>> cities = jdbcTemplate.queryForList("SELECT name FROM city");
    logger.info("System has " + cities.size() + " cities");

    return dataSource;
  }
}
