package com.nc.quarkus.config;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nc.quarkus.topics.util.JSON;

import io.quarkus.jackson.ObjectMapperCustomizer;

@Singleton
public class JacksonCustomizer implements ObjectMapperCustomizer {

	// should be redundant,
	@Override
	public void customize(ObjectMapper objectMapper) {
		objectMapper.setDefaultPrettyPrinter(JSON.pp);
	}

}
