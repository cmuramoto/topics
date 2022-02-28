package com.nc.topics.quarkus.repositories.internal;

import org.springframework.stereotype.Repository;

import com.nc.topics.quarkus.domain.internal.movie.Movie;
import com.nc.topics.quarkus.repositories.base.AbstractEntityRepository;

@Repository
public interface MovieRepository extends AbstractEntityRepository<Movie> {

}
