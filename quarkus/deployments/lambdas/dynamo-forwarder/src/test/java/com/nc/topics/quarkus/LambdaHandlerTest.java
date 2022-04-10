package com.nc.topics.quarkus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class LambdaHandlerTest {

	@Test
	public void testSimpleLambdaGuardedSuccess() throws Exception {
		// you test your lambas by invoking on http://localhost:8081
		// this works in dev mode too

		InputObject in = new InputObject();
		in.setMax(41);
		given().contentType("application/json").accept("application/json").body(in).when().post().then().statusCode(200).body(containsString("result"));
	}

	@Test
	public void testSimpleLambdaSuccess() throws Exception {
		// you test your lambas by invoking on http://localhost:8081
		// this works in dev mode too

		InputObject in = new InputObject();
		in.setMax(35);
		given().contentType("application/json").accept("application/json").body(in).when().post().then().statusCode(200).body(containsString("result"));
	}

}
