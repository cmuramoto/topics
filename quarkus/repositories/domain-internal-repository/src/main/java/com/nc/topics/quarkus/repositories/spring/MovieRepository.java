package com.nc.topics.quarkus.repositories.spring;

import org.springframework.stereotype.Repository;

import com.nc.topics.quarkus.domain.internal.movie.Movie;

@Repository
public interface MovieRepository extends AbstractEntityRepository<Movie> {

}
