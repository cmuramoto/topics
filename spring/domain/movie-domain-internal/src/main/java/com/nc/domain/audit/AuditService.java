package com.nc.domain.audit;

import java.util.Optional;

public interface AuditService {

	Optional<AuditAction> findById(Integer id);

	AuditAction save(AuditAction action);

}
