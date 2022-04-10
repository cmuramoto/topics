package com.nc.topics.quarkus.xa.test.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.cfg.AvailableSettings;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.LoggerFactory;

import com.nc.quarkus.topics.util.JSON;
import com.nc.topics.quarkus.domain.internal.movie.AppUser;
import com.nc.topics.quarkus.domain.internal.movie.Movie;
import com.nc.topics.quarkus.repositories.spring.AuditActionRepository;
import com.nc.topics.quarkus.repositories.spring.MovieRepository;
import com.nc.topics.quarkus.xa.ComboService;
import com.nc.topics.quarkus.xa.ForcedException;

import io.quarkus.arc.ArcUndeclaredThrowableException;
import io.quarkus.hibernate.orm.runtime.PersistenceUnitsHolder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class XATestTemplate {

	static void dump(boolean finished) {
		var descriptors = PersistenceUnitsHolder.getPersistenceUnitDescriptors();
		var log = LoggerFactory.getLogger(XATestTemplate.class);

		record PUInfo(String name, List<String> managed, String dialect, String txType) {
		}

		var infos = new ArrayList<>(2);

		for (var desc : descriptors) {
			var name = desc.getName();
			var txType = desc.getTransactionType().toString();
			var managed = desc.getManagedClassNames().stream().map(n -> {
				var ix = n.lastIndexOf('.');

				return ix > 0 ? n.substring(ix + 1) : n;
			}).toList();
			var dialect = desc.getProperties().getProperty(AvailableSettings.DIALECT);

			// log.info("PU:{}=>({}) Dialect: {}. TxType: {}", name, managed, dialect, txType);

			infos.add(new PUInfo(name, managed, dialect, txType));
		}

		log.info("{} Persistence Units: \n{}\n", finished ? "Finished test with " : "Starting test with ", JSON.pretty(infos));
	}

	@AfterAll
	public static void finished() {
		dump(false);
	}

	@BeforeAll
	public static void starting() {
		dump(false);
	}

	@Inject
	ComboService cs;

	@Inject
	AuditActionRepository audits;

	@Inject
	MovieRepository movies;

	@Inject
	Logger log;

	final AppUser dummy = new AppUser("dummy", "pass");

	private Movie newMovie(int i) {
		var m = new Movie();
		m.setImdbID("imdb" + i);
		m.setPoster("p" + i);
		m.setScore(i);
		m.setTitle("title" + i);
		m.setTotalVotes(i * 10);

		return m;
	}

	@Test
	@Order(0)
	public void will_save_movie_and_save_audit() {
		var movie = newMovie(0);

		cs.saveMovieAndAudit(dummy, movie);

		assertEquals(1, audits.count());
		assertEquals(1, movies.count());
	}

	@Test
	@Order(1)
	public void will_save_movie_and_save_audit_then_throw_exception() {
		assertEquals(1, audits.count());
		assertEquals(1, movies.count());

		var movie = newMovie(0);

		assertThrows(ForcedException.class, () -> {
			cs.saveMovieAndAuditThenThrow(dummy, movie);
		});

		assertEquals(1, audits.count());
		assertEquals(1, movies.count());
	}

	@Test
	@Order(2)
	public void will_save_movie_delay_more_than_tx_timeout_then_save_audit() {
		assertEquals(1, audits.count());
		assertEquals(1, movies.count());

		var movie = newMovie(0);

		// quarkus does not unwrap RollbackException
		assertThrows(ArcUndeclaredThrowableException.class, () -> {
			cs.saveMovieAndTimeoutBeforSavingAuditAudit(dummy, movie);
		});

		assertEquals(1, audits.count());
		assertEquals(1, movies.count());
	}

	@Test
	@Order(4)
	public void will_save_movie_in_new_tx_and_save_audit_then_throw() {
		assertEquals(1, audits.count());
		assertEquals(1, movies.count());

		var movie = newMovie(0);

		assertThrows(ForcedException.class, () -> {
			cs.saveMovieInNewTxAndAuditThenThrow(dummy, movie);
		});

		assertEquals(1, audits.count());
		assertEquals(2, movies.count());
	}

	@Test
	@Order(5)
	public void will_save_movie_in_new_tx_then_delay_then_save_audit_then_throw() {
		assertEquals(1, audits.count());
		assertEquals(2, movies.count());

		var movie = newMovie(0);

		// quarkus does not unwrap RollbackException
		assertThrows(ArcUndeclaredThrowableException.class, () -> {
			cs.saveMovieInNewTxAndTimeoutBeforSavingAuditAudit(dummy, movie);
		});

		assertEquals(1, audits.count());
		assertEquals(3, movies.count());
	}

	@Test
	@Order(6)
	public void will_xpunge_all() {
		try {
			audits.deleteAll();
			movies.deleteAll();

			assertEquals(0, audits.count());
			assertEquals(0, movies.count());
		} catch (IllegalStateException e) {
			// https://github.com/quarkusio/quarkus/issues/24128
			log.infof("Spring does not support multiple PU's yet {}", e.getMessage());
		}
	}

}
