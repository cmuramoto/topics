package com.nc.topics.quarkus.xa.profiles;

import java.util.Map;

import com.nc.topics.quarkus.xa.TxOptions;

import io.quarkus.test.junit.QuarkusTestProfile;

public class XAProfileTemplate implements QuarkusTestProfile {

	@Override
	public Map<String, String> getConfigOverrides() {
		return Map.of( //
				"quarkus.transaction-manager.default-transaction-timeout", Long.toString(TxOptions.TX_TIMEOUT) //
		);
	}

}
