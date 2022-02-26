package com.nc.subtopics.xa.h2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import com.nc.domain.internal.AppUser;
import com.nc.domain.internal.DomainException;
import com.nc.domain.internal.Movie;
import com.nc.repositories.jpa.audit.AuditRepo;
import com.nc.repositories.jpa.internal.MovieRepository;
import com.nc.subtopics.xa.h2.config.AppComboConfig;
import com.nc.subtopics.xa.ha.services.api.ComboService;

@SpringBootTest
@ContextConfiguration(classes = AppComboConfig.class)
public class ComboTests {

	final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	ComboService combo;

	@Autowired
	AuditRepo audits;

	@Autowired
	MovieRepository movies;

	final AppUser dummy = new AppUser();

	{
		dummy.setName("dummy");
	}

	@Test
	public void _00_test_save_and_commit_both_in_same_tx() {
		var movie = new Movie();
		movie.setTitle("0");
		movie.setImdbID("0");

		combo.saveAndAudit(dummy, movie);

		var found = movies.findOneByTitle("0").orElseThrow();

		assertEquals(movie.getId(), found.getId());
		assertEquals(movie.getTitle(), found.getTitle());

		var all = audits.findAll(PageRequest.of(0, 100)).toList();

		assertEquals(1, all.size());
	}

	@Test
	public void _01_test_save_both_then_fail_after_saving_audit_in_same_tx() {
		var movie = new Movie();
		movie.setTitle("1");
		movie.setImdbID("1");

		try {
			combo.saveMovieOnlyThenFail(dummy, movie);
			fail("should not reach here");
		} catch (DomainException e) {
			log.info(e.getMessage());
		}

		var found = movies.findOneByTitle("2").orElse(null);

		assertNull(found);

		var all = audits.findAll(PageRequest.of(0, 100)).toList();

		assertEquals(1, all.size());
	}

	@Test
	public void _02_test_save__movie_and_fail_audit_both_in_same_tx() {
		var movie = new Movie();
		movie.setTitle("2");
		movie.setImdbID("2");

		try {
			combo.saveMovieAndFailAudit(dummy, movie);
			fail("should not reach here");
		} catch (DomainException e) {
			log.info(e.getMessage());
		}

		var found = movies.findOneByTitle("2").orElse(null);

		assertNull(found);

		var all = audits.findAll(PageRequest.of(0, 100)).toList();

		assertEquals(1, all.size());
	}

	@Test
	public void _03_test_save__movie_in_new_tx_and_fail_audit_original_tx() {
		var movie = new Movie();
		movie.setTitle("3");
		movie.setImdbID("3");

		try {
			combo.saveMovieInNewTxAndFailAudit(dummy, movie);
			fail("should not reach here");
		} catch (DomainException e) {
			log.info(e.getMessage());
		}

		var found = movies.findOneByTitle("3").orElseThrow();

		assertEquals(movie.getId(), found.getId());
		assertEquals(movie.getTitle(), found.getTitle());

		assertEquals(2, movies.findAll(PageRequest.of(0, 100)).toList().size());

		var all = audits.findAll(PageRequest.of(0, 100)).toList();

		assertEquals(1, all.size());
	}

}
