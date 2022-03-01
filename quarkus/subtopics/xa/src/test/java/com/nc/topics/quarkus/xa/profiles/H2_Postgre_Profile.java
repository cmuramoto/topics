package com.nc.topics.quarkus.xa.profiles;

public class H2_Postgre_Profile extends XAProfileTemplate {

	@Override
	public String getConfigProfile() {
		return "h2_postgre";
	}

}
