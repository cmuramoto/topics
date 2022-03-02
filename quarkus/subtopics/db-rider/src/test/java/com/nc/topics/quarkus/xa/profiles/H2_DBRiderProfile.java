package com.nc.topics.quarkus.xa.profiles;

public class H2_DBRiderProfile extends DBRiderProfileTemplate {

	@Override
	public String getConfigProfile() {
		return "h2";
	}

}
