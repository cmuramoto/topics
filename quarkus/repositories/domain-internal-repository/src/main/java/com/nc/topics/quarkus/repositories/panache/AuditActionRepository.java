package com.nc.topics.quarkus.repositories.panache;

import javax.enterprise.context.ApplicationScoped;

import com.nc.topics.quarkus.domain.internal.audit.AuditAction;

@ApplicationScoped
public class AuditActionRepository implements AbstractPanacheEntityRepository<AuditAction> {

}