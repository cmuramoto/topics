package com.nc.subtopics.xa.ha.services.api;

import com.nc.domain.internal.AppUser;
import com.nc.domain.internal.Movie;

public interface ComboService {

	void saveAndAudit(AppUser requestor, Movie movie);

	void saveMovieOnlyThenFail(AppUser requestor, Movie movie);

	void saveMovieAndFailAudit(AppUser requestor, Movie movie);

	void saveMovieInNewTxAndFailAudit(AppUser requestor, Movie movie);

}
