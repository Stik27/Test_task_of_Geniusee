package com.stik.cinema.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.stik.cinema.dto.MovieDto;
import com.stik.cinema.dto.MovieInputDto;


public interface MovieService {
	MovieDto findById(UUID id);

	Page<MovieDto> searchMovies(MovieInputDto movieInputDto, Pageable pageable);

	void delete(UUID id);

	MovieDto create(MovieInputDto movieInput);

	MovieDto update(MovieInputDto movieInput);

}
