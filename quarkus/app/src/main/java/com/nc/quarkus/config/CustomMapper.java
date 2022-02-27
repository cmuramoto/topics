package com.nc.quarkus.config;

import javax.enterprise.inject.Instance;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nc.quarkus.util.JSON;

import io.quarkus.jackson.ObjectMapperCustomizer;

public class CustomMapper {

	@Singleton
	ObjectMapper objectMapper(Instance<ObjectMapperCustomizer> customizers) {
		ObjectMapper mapper = JSON.newMapper();

		for (ObjectMapperCustomizer customizer : customizers) {
			customizer.customize(mapper);
		}

		return mapper;
	}

}
