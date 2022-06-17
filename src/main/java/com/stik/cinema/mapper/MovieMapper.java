package com.stik.cinema.mapper;

import org.mapstruct.Mapper;

import com.stik.cinema.dto.MovieDto;
import com.stik.cinema.persistance.Movie;

@Mapper(componentModel = "spring")
public interface MovieMapper {
	MovieDto toDto(Movie movie);
}

