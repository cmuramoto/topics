package com.nc.topics.quarkus.xa.profiles;

public class H2_MySql_Profile extends XAProfileTemplate {

	@Override
	public String getConfigProfile() {
		return "h2-mysql";
	}

}
