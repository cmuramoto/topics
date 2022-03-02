package com.nc.topics.quarkus.xa.test;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.cfg.AvailableSettings;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.LoggerFactory;

import com.nc.quarkus.topics.util.JSON;
import com.nc.topics.quarkus.repositories.internal.MovieRepository;

import io.quarkus.hibernate.orm.runtime.PersistenceUnitsHolder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class DBRiderTestTemplate {

	static void dump(boolean finished) {
		var descriptors = PersistenceUnitsHolder.getPersistenceUnitDescriptors();
		var log = LoggerFactory.getLogger(DBRiderTestTemplate.class);

		record PUInfo(String name, List<String> managed, String dialect, String txType) {
		}

		var infos = new ArrayList<>(2);

		for (var desc : descriptors) {
			var name = desc.getName();
			var txType = desc.getTransactionType().toString();
			var managed = desc.getManagedClassNames().stream().map(n -> {
				var ix = n.lastIndexOf('.');

				return ix > 0 ? n.substring(ix + 1) : n;
			}).toList();
			var dialect = desc.getProperties().getProperty(AvailableSettings.DIALECT);

			// log.info("PU:{}=>({}) Dialect: {}. TxType: {}", name, managed, dialect, txType);

			infos.add(new PUInfo(name, managed, dialect, txType));
		}

		log.info("{} Persistence Units: \n{}\n", finished ? "Finished test with " : "Starting test with ", JSON.pretty(infos));
	}

	@Inject
	MovieRepository movies;

	public void checkLoadedFromFile() {

	}

}
