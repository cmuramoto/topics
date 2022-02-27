package com.nc.domain.audit;

import javax.persistence.Entity;

import com.nc.domain.base.AbstractEntity;

@Entity
public class AuditAction extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
