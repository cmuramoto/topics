package com.nc.topics.quarkus.xa.profiles;

public class H2_H2_Profile extends XAProfileTemplate {

	@Override
	public String getConfigProfile() {
		return "h2_h2";
	}

}
