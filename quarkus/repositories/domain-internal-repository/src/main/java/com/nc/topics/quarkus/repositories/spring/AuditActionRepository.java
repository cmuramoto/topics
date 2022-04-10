package com.nc.topics.quarkus.repositories.spring;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nc.topics.quarkus.domain.internal.audit.AuditAction;

import io.quarkus.hibernate.orm.PersistenceUnit;

@Repository
@PersistenceUnit("audit")
public interface AuditActionRepository extends AbstractEntityRepository<AuditAction> {

	@Query("select * from AuditAction a")
	public List<AuditAction> top();

}
