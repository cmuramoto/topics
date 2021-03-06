package com.nc.topics.quarkus.repositories.test;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.nc.topics.quarkus.domain.internal.audit.AuditAction;
import com.nc.topics.quarkus.domain.internal.movie.Movie;
import com.nc.topics.quarkus.repositories.panache.AuditActionRepository;
import com.nc.topics.quarkus.repositories.panache.MovieRepository;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TwoPersistenceUnitPanacheTest {

	@Inject
	AuditActionRepository audits;

	@Inject
	MovieRepository movies;

	@Inject
	@Named("audit")
	EntityManagerFactory auditFactory;

	@Inject
	@Named("movie")
	EntityManagerFactory movieFactory;

	@Inject
	Logger log;

	@Transactional
	<T> T findById(EntityManagerFactory factory, Class<T> type, Integer id) {
		var em = factory.createEntityManager();
		try {
			return em.find(type, id);
		} finally {
			em.close();
		}
	}

	@Transactional
	<T> T inTx(Supplier<T> s) {
		return s.get();
	}

	@Test
	@Order(0)
	public void will_save_and_retrieve_audit() {
		var audit = new AuditAction();
		audit.setIssuer("mary");
		audit.setAction("something");
		audit.setTimestamp(LocalDateTime.now());

		var saved = inTx(() -> audits.save(audit));

		var found = audits.findById(saved.getId());

		Assertions.assertNotSame(saved, found);
		Assertions.assertEquals(saved.getAction(), found.getAction());

		found = findById(auditFactory, AuditAction.class, saved.getId());
		Assertions.assertNotSame(audit, found);
		Assertions.assertEquals(audit.getAction(), found.getAction());

		try {
			found = findById(movieFactory, AuditAction.class, audit.getId());
			Assertions.fail("Should not find AuditAction in movie PU");
		} catch (Exception e) {
			log.infof("Ok! caught: %s", e.getClass());
		}
	}

	@Test
	@Order(1)
	public void will_save_and_retrieve_movie() {
		var movie = new Movie();
		movie.setTitle("title");
		movie.setImdbID("id");
		movie.setPoster("poster");

		var saved = inTx(() -> movies.save(movie));

		var found = movies.findById(saved.getId());

		Assertions.assertNotSame(movie, found);
		Assertions.assertEquals(movie.getTitle(), found.getTitle());

		found = findById(movieFactory, Movie.class, found.getId());
		Assertions.assertNotSame(movie, found);
		Assertions.assertEquals(movie.getTitle(), found.getTitle());

		try {
			found = findById(auditFactory, Movie.class, found.getId());
			Assertions.fail("Should not find Movie in audit PU");
		} catch (Exception e) {
			log.infof("Ok! caught: %s", e.getClass());
		}
	}
}
