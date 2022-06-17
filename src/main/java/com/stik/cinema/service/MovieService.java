package com.stik.cinema.service;

import java.util.UUID;

import com.stik.cinema.dto.MovieDto;
import com.stik.cinema.dto.MovieInputDto;


public interface MovieService {
	MovieDto findById(UUID id);

	void delete(UUID id);

	MovieDto create(MovieInputDto movieInput);

	MovieDto update(MovieInputDto movieInput);

}
