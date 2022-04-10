package com.nc.quarkus.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingResourceTest {

	@Test
	public void testEchoEndpoint() {
		var h = given().when().get("/hello/echo").then().statusCode(200).extract().as(Map.class).get("http-headers");

		Assert.assertNotNull(h);
	}

	@Test
	public void testHelloEndpoint() {
		given().when().get("/hello").then().statusCode(200).body(is("Hello RESTEasy"));
	}

	@Test
	public void testHelloWorldEndpoint() {
		given().when().get("/hello/world").then().statusCode(200).body(is("Hello World RESTEasy"));
	}

}