package com.nc.topics.quarkus.repositories.base;

import java.util.HashMap;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;

import io.quarkus.hibernate.orm.panache.common.runtime.AbstractJpaOperations;
import io.quarkus.hibernate.orm.runtime.PersistenceUnitsHolder;
import io.quarkus.runtime.StartupEvent;

@Singleton
public class Bootstrapper {

	public void onEvent(@Observes StartupEvent ev) {
		var descs = PersistenceUnitsHolder.getPersistenceUnitDescriptors();

		var map = new HashMap<String, String>();

		descs.forEach(desc -> {
			var name = desc.getName();
			var types = desc.getManagedClassNames();

			for (var type : types) {
				map.put(type, name);
			}
		});

		AbstractJpaOperations.setEntityToPersistenceUnit(map);
	}
}
