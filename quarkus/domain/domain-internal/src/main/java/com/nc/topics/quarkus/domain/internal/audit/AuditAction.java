package com.nc.topics.quarkus.domain.internal.audit;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import com.nc.topics.quarkus.domain.internal.base.AbstractEntity;

@Entity
public class AuditAction extends AbstractEntity {

	String issuer;

	String action;

	LocalDateTime timestamp;

	public String getAction() {
		return action;
	}

	public String getIssuer() {
		return issuer;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}