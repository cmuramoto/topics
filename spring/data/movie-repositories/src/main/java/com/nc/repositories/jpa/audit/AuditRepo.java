package com.nc.repositories.jpa.audit;

import org.springframework.stereotype.Repository;

import com.nc.domain.audit.AuditAction;
import com.nc.repositories.jpa.internal.AbstractEntityRepository;

@Repository
public interface AuditRepo extends AbstractEntityRepository<AuditAction> {

}
