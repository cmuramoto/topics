package com.nc.subtopics.xa.ha.services.imp;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nc.domain.audit.AuditAction;
import com.nc.domain.internal.AppUser;
import com.nc.domain.internal.DomainException;
import com.nc.domain.internal.Movie;
import com.nc.domain.internal.MovieService;
import com.nc.repositories.jpa.audit.AuditRepo;
import com.nc.repositories.jpa.internal.MovieRepository;
import com.nc.subtopics.xa.ha.services.api.ComboService;

@Service
@Transactional
public class ComboServiceImp implements ComboService {

	@Autowired
	MovieRepository movies;

	@Autowired
	AuditRepo audits;

	@Autowired
	MovieService movieService;

	@Override
	public void saveAndAudit(AppUser requestor, Movie movie) {
		movies.save(movie);

		var audit = new AuditAction();
		audit.setAction("Saved by " + requestor.getName());

		audits.save(audit);
	}

	@Override
	public void saveMovieAndFailAudit(AppUser requestor, Movie movie) {
		movies.save(movie);

		throw new DomainException("Forcing exception after saving movie", null);
	}

	@Override
	public void saveMovieInNewTxAndFailAudit(AppUser requestor, Movie movie) {
		movieService.saveInNewTx(movie);

		var audit = new AuditAction();
		audit.setAction("Saved by " + requestor.getName());

		audits.save(audit);

		throw new DomainException("Forcing exception after saving both entities, with movie in new tx", null);
	}

	@Override
	public void saveMovieOnlyThenFail(AppUser requestor, Movie movie) {
		movies.save(movie);

		var audit = new AuditAction();
		audit.setAction("Saved by " + requestor.getName());

		audits.save(audit);

		throw new DomainException("Forcing exception after saving both entities", null);
	}
}