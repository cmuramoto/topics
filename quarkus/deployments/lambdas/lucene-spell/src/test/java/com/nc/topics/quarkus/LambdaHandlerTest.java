package com.nc.topics.quarkus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.nc.topics.spell.CreateIndex;
import com.nc.topics.spell.frontend.InputObject;

import __.java.lang.Prims;
import io.quarkus.test.junit.QuarkusTest;
import lombok.experimental.ExtensionMethod;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtensionMethod({ Prims.class })
public class LambdaHandlerTest {

	static final int MAX = 10000;

	@BeforeAll
	public static void checkIndex() {
		var path = Paths.get("index");
		if (!Files.exists(path)) {
			new CreateIndex().create(path, MAX);
		}
	}

	@Test
	@Order(1)
	public void testCallPerf() throws Exception {
		var max = 1000;

		var elapsed = System.currentTimeMillis();

		var stats = IntStream.range(0, max).parallel().mapToLong(i -> {
			var in = new InputObject();
			in.setValue(i);

			var now = System.nanoTime();
			var res = given() //
					.contentType("application/json").accept("application/json") //
					.body(in) //
					.when().post() //
					.then() //
					.statusCode(200);
			var e = System.nanoTime() - now;
			res.body(containsString(Integer.toHexString(in.getValue())));

			return e;
		}).summaryStatistics();

		elapsed = System.currentTimeMillis() - elapsed;

		var total = stats.getSum().toMillis();
		var average = stats.getAverage().toMillis();
		log.infof("Elapsed  %d ms. Total cpu: %.2f. QPs: %.2f. Avg: %.2fms. Min: %.2fms. Max: %.2fms", elapsed, total, (1000d * max) / elapsed, average, stats.getMin().toMillis(), stats.getMax().toMillis());
	}

	@Test
	@Order(0)
	public void testSimpleCall() throws Exception {
		var in = new InputObject();
		in.setValue(35);
		given()//
				.contentType("application/json").accept("application/json").body(in).when().post()//
				.then()//
				.statusCode(200).body(containsString(Integer.toHexString(in.getValue())));
	}
}
