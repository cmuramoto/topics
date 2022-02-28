package com.nc.topics.quarkus.services.imp.greet;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.Transactional;

import com.nc.topics.quarkus.services.api.greet.GreetService;

@Singleton
public class GreetServiceImpl implements GreetService {

	@Inject
	TransactionManager tm;

	@Override
	@Transactional
	public String greet() {
		try {
			var tx = tm.getTransaction();
			return tx.toString() + LocalDateTime.now().toString();
		} catch (SystemException e) {
			throw new RuntimeException(e);
		}
	}

}
