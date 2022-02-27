package com.nc.quarkus.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingResourceTest {

	@Test
	public void testHelloEndpoint() {
		given().when().get("/hello").then().statusCode(200).body(is("Hello RESTEasy"));
	}

	@Test
	public void testHelloWorldEndpoint() {
		given().when().get("/hello/world").then().statusCode(200).body(is("Hello World RESTEasy"));
	}

}