package com.nc.topics.quarkus.domain.internal.base;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	public Integer getId() {
		return id;
	}
}
