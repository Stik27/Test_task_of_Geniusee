package com.stik.cinema.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stik.cinema.persistance.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {
	Optional<Movie> findByName(String name);
}

