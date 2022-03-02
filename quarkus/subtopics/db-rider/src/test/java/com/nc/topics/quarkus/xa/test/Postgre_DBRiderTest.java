package com.nc.topics.quarkus.xa.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.nc.topics.quarkus.xa.profiles.Postgre_DBRiderProfile;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(Postgre_DBRiderProfile.class)
@DBRider
public class Postgre_DBRiderTest extends DBRiderTestTemplate {

	@Test
	@Order(0)
	@DataSet(value = "scripts/movies.yml", cleanAfter = true)
	@ExpectedDataSet(value = "scripts/movies-expected.yml")
	@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
	public void checkLoadAndVerify() {
		var all = movies.findAll(PageRequest.of(0, Integer.MAX_VALUE));
		var els = all.getTotalElements();

		Assertions.assertEquals(2, els);
	}

	@Test
	@Order(1)
	public void checkPostLoad() {
		var all = movies.findAll(PageRequest.of(0, Integer.MAX_VALUE));
		var els = all.getTotalElements();

		Assertions.assertEquals(0, els);
	}

}