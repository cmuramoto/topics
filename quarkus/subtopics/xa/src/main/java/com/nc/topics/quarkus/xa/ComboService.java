package com.nc.topics.quarkus.xa;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

import com.nc.topics.quarkus.domain.internal.audit.AuditAction;
import com.nc.topics.quarkus.domain.internal.movie.AppUser;
import com.nc.topics.quarkus.domain.internal.movie.Movie;
import com.nc.topics.quarkus.repositories.spring.AuditActionRepository;
import com.nc.topics.quarkus.repositories.spring.MovieRepository;

@Singleton
public class ComboService {

	static void delay(long s) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(s));
	}

	@Inject
	MovieRepository movies;

	@Inject
	AuditActionRepository audits;

	@Inject
	Logger log;

	private AuditAction createAction(AppUser requestor, Movie movie) {
		var action = new AuditAction();
		action.setIssuer(requestor.getName());
		action.setAction("Creating " + movie.getTitle());
		action.setTimestamp(LocalDateTime.now());

		return action;
	}

	@Transactional(REQUIRES_NEW)
	public void saveInNewTx(Movie movie) {
		movies.save(movie);
	}

	@Transactional
	public void saveMovieAndAudit(AppUser requestor, Movie movie) {
		movies.save(movie);

		var action = createAction(requestor, movie);

		audits.save(action);
	}

	@Transactional
	public void saveMovieAndAuditThenThrow(AppUser requestor, Movie movie) {
		movies.save(movie);
		log.info("Movie saved");

		var action = createAction(requestor, movie);

		audits.save(action);

		throw new ForcedException();
	}

	@Transactional
	public void saveMovieAndTimeoutBeforSavingAuditAudit(AppUser requestor, Movie movie) {
		movies.save(movie);

		log.info("Movie saved");

		log.info("Parking");

		delay(TxOptions.TX_TIMEOUT * 2);

		log.info("Saving Audit");

		var action = createAction(requestor, movie);

		audits.save(action);

		log.info("Audit saved");
	}

	@Transactional
	public void saveMovieInNewTxAndAuditThenThrow(AppUser requestor, Movie movie) {
		saveInNewTx(movie);
		log.info("Movie saved");

		var action = createAction(requestor, movie);

		audits.save(action);

		log.info("Audit saved");

		throw new ForcedException();
	}

	@Transactional
	public void saveMovieInNewTxAndTimeoutBeforSavingAuditAudit(AppUser requestor, Movie movie) {
		saveInNewTx(movie);

		log.info("Movie saved");

		log.info("Parking");

		delay(TxOptions.TX_TIMEOUT * 2);

		log.info("Saving Audit");

		var action = createAction(requestor, movie);

		audits.save(action);

		log.info("Audit saved");
	}
}