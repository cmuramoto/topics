package com.nc.topics.quarkus.xa.profiles;

import java.util.Map;

import com.nc.topics.quarkus.xa.TxOptions;

import io.quarkus.test.junit.QuarkusTestProfile;

public class H2_H2_Profile implements QuarkusTestProfile {

	@Override
	public Map<String, String> getConfigOverrides() {
		return Map.of( //
				"quarkus.transaction-manager.default-transaction-timeout", Long.toString(TxOptions.TX_TIMEOUT) //
		);
	}

	@Override
	public String getConfigProfile() {
		return "h2_h2";
	}

}
