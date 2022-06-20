package com.stik.cinema.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.stik.cinema.persistance.Movie;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MovieSpecifications {

	public static Specification<Movie> likeName(String name) {
		if(name == null) {
			return null;
		}
		return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
	}

	public static Specification<Movie> equalCost(Integer cost) {
		if(cost == null) {
			return null;
		}
		return (root, query, cb) -> cb.equal(root.get("cost"), cost);

	}

	public static Specification<Movie> equalReleaseDate(LocalDate releaseDate) {
		if(releaseDate == null) {
			return null;
		}
		return (root, query, cb) -> cb.equal(root.get("releaseDate"), releaseDate);
	}
}
