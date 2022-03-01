package com.nc.topics.quarkus.xa.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.inject.Inject;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.nc.topics.quarkus.domain.internal.movie.AppUser;
import com.nc.topics.quarkus.domain.internal.movie.Movie;
import com.nc.topics.quarkus.repositories.audit.AuditActionRepository;
import com.nc.topics.quarkus.repositories.internal.MovieRepository;
import com.nc.topics.quarkus.xa.ComboService;
import com.nc.topics.quarkus.xa.ForcedException;
import com.nc.topics.quarkus.xa.profiles.H2_H2_Profile;

import io.quarkus.arc.ArcUndeclaredThrowableException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestProfile(H2_H2_Profile.class)
public class TestBase {

	@Inject
	ComboService cs;

	@Inject
	AuditActionRepository audits;

	@Inject
	MovieRepository movies;

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
}
