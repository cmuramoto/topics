package com.nc.services.audit;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nc.domain.audit.AuditAction;
import com.nc.domain.audit.AuditService;
import com.nc.repositories.jpa.audit.AuditRepo;

@Service
@Transactional
public class AuditServiceImp implements AuditService {

	@Autowired
	AuditRepo audits;

	@Override
	public Optional<AuditAction> findById(Integer id) {
		return audits.findById(id);
	}

	@Override
	public AuditAction save(AuditAction action) {
		return audits.save(action);
	}

}
