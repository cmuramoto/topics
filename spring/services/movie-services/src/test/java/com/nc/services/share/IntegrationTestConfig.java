package com.nc.services.share;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:test.properties" })
@ImportResource("classpath:persistence.xml")
@EnableJpaRepositories(basePackages = "com.nc.repositories.jpa.internal")
@ComponentScan(basePackages = { "com.nc.services.internal", "com.nc.services.frontend" })
public class IntegrationTestConfig {

}
