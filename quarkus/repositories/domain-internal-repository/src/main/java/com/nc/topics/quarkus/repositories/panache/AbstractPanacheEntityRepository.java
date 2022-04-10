package com.nc.topics.quarkus.repositories.panache;

import java.util.stream.Stream;

import com.nc.topics.quarkus.domain.internal.base.AbstractEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

public interface AbstractPanacheEntityRepository<T extends AbstractEntity> extends PanacheRepositoryBase<T, Integer> {

	default Stream<ID> ids() {
		return this.findAll().project(ID.class).stream();
	}

	default T save(T entity) {
		persist(entity);
		return entity;
	}

}
