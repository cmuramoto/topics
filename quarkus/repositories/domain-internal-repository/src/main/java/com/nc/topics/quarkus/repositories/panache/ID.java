package com.nc.topics.quarkus.repositories.panache;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ID {

	final int id;

	public ID(int id) {
		this.id = id;
	}

}
