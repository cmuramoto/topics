package com.nc.topics.quarkus.domain.internal.movie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import com.nc.topics.quarkus.domain.internal.base.AbstractEntity;

@Entity
public class Movie extends AbstractEntity {

	@NotBlank
	String title;

	@NotBlank
	String imdbID;

	@Column(columnDefinition = "TEXT")
	String poster;

	@PositiveOrZero
	float score;

	@PositiveOrZero
	int totalVotes;

	public String getImdbID() {
		return imdbID;
	}

	public String getPoster() {
		return poster;
	}

	public float getScore() {
		return score;
	}

	public String getTitle() {
		return title;
	}

	public int getTotalVotes() {
		return totalVotes;
	}

	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTotalVotes(int totalVotes) {
		this.totalVotes = totalVotes;
	}
}
