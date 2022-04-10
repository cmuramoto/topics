package com.nc.topics.quarkus.xa.test.panache;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.nc.topics.quarkus.xa.profiles.H2_MySql_Profile;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(H2_MySql_Profile.class)
public class H2_MySQL_XAIntegrationTest extends XATestTemplate {

	@Test
	@Order(Integer.MIN_VALUE)
	public void cleanup() {
		super.will_xpunge_all();
	}

}
