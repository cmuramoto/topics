package com.nc.topics.spell;

import io.quarkus.test.junit.QuarkusTestProfile;

public class CreateIndexProfile implements QuarkusTestProfile {

	@Override
	public String getConfigProfile() {
		return "index";
	}
}
