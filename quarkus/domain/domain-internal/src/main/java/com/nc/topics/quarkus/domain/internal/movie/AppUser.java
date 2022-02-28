package com.nc.topics.quarkus.domain.internal.movie;

import javax.persistence.Entity;

import com.nc.topics.quarkus.domain.internal.base.AbstractEntity;

@Entity
public class AppUser extends AbstractEntity {

	String name;

	String pass;

	public String getName() {
		return name;
	}

	public String getPass() {
		return pass;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

}
