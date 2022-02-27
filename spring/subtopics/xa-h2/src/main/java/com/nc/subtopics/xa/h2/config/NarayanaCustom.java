package com.nc.subtopics.xa.h2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import me.snowdrop.boot.narayana.core.properties.NarayanaProperties;

@Configuration
public class NarayanaCustom {

	@Bean
	@Primary
	public NarayanaProperties nps() {
		var np = new NarayanaProperties();
		np.setDefaultTimeout(60 * 1000 * 1000);

		return np;
	}

}
