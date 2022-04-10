package com.nc.topics.quarkus.repositories.panache;

import javax.enterprise.context.ApplicationScoped;

import com.nc.topics.quarkus.domain.internal.movie.Movie;

@ApplicationScoped
public class MovieRepository implements AbstractPanacheEntityRepository<Movie> {

}
