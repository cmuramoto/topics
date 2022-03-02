package com.nc.topics.quarkus.xa.profiles;

import java.util.Map;

import com.nc.topics.quarkus.xa.TxOptions;

import io.quarkus.test.junit.QuarkusTestProfile;

public class DBRiderProfileTemplate implements QuarkusTestProfile {

	@Override
	public Map<String, String> getConfigOverrides() {
		return Map.of( //
				"com.arjuna.ats.arjuna.common.propertiesFile", "custom-tx.xml", //
				"quarkus.transaction-manager.default-transaction-timeout", Long.toString(TxOptions.TX_TIMEOUT) //
		);
	}

}
