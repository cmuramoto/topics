package com.nc.service.impl.greet;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.nc.service.api.greet.GreetService;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.common.constraint.Assert;

@QuarkusTest
public class GreetServiceTest {

	@Inject
	GreetService gs;

	@Test
	public void will_greet() {
		Assert.assertTrue(gs.greet().indexOf("Transaction") >= 0);
	}

}
