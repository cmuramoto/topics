package com.nc.subtopics.xa.h2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.nc.domain.audit.AuditAction;
import com.nc.domain.audit.AuditService;
import com.nc.repositories.jpa.audit.AuditRepo;
import com.nc.subtopics.xa.h2.config.AppAuditConfig;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@ContextConfiguration(classes = AppAuditConfig.class)
public class AuditOnlyTests {

	@Autowired
	AuditService audits;

	@Autowired
	AuditRepo repo;

	@Test
	public void _00_will_save_audit() {
		var action = new AuditAction();
		action.setAction("test");
		action = audits.save(action);

		Assertions.assertNotNull(action.getId());

		var rec = audits.findById(action.getId()).orElseThrow();

		Assertions.assertEquals(action.getAction(), rec.getAction());

		action = new AuditAction();
		action.setAction("test2");
		action = repo.save(action);

		rec = repo.findById(action.getId()).orElseThrow();

		Assertions.assertEquals(action.getAction(), rec.getAction());
	}

}
