package com.nc.topics.quarkus.repositories.audit;

import org.springframework.stereotype.Repository;

import com.nc.topics.quarkus.domain.internal.audit.AuditAction;
import com.nc.topics.quarkus.repositories.base.AbstractEntityRepository;

import io.quarkus.hibernate.orm.PersistenceUnit;

@Repository
@PersistenceUnit("audit")
public interface AuditActionRepository extends AbstractEntityRepository<AuditAction> {

}
