package com.nc.topics.quarkus.xa.profiles;

public class Postgre_DBRiderProfile extends DBRiderProfileTemplate {

	@Override
	public String getConfigProfile() {
		return "pg";
	}

}
