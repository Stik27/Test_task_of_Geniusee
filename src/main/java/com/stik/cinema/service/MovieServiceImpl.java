package com.stik.cinema.service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stik.cinema.dto.MovieDto;
import com.stik.cinema.dto.MovieInputDto;
import com.stik.cinema.exception.CinemaException;
import com.stik.cinema.exception.ErrorCode;
import com.stik.cinema.mapper.MovieMapper;
import com.stik.cinema.persistance.Movie;
import com.stik.cinema.repository.MovieRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {
	private final MovieRepository movieRepository;

	private final OrdersService ordersService;
	private final MovieMapper mapper;

	@Override
	public MovieDto findById(UUID id) {
		Movie byId = movieRepository.findById(id)
				.orElseThrow(() -> new CinemaException(ErrorCode.NOT_FOUND, "movie not found"));
		return mapper.toDto(byId);
	}

	@Transactional
	@Override
	public void delete(UUID id) {
		ordersService.deleteAllByMovieId(id);
		movieRepository.deleteById(id);
	}

	@Transactional
	@Override
	public MovieDto create(MovieInputDto movieInput) {
		Optional<Movie> byName = movieRepository.findByName(movieInput.getName());

		if(byName.isPresent()) {
			throw new CinemaException(ErrorCode.ALREADY_OCCUPIED, "Such a name is already occupied");
		}

		if(Objects.nonNull(movieInput.getName())
				&& Objects.nonNull(movieInput.getReleaseDate())
				&& Objects.nonNull(movieInput.getCost())) {
			Movie newMovie = new Movie();
			newMovie.setId(UUID.randomUUID());
			newMovie.setName(movieInput.getName());
			newMovie.setReleaseDate(movieInput.getReleaseDate());
			newMovie.setCost(movieInput.getCost());

			movieRepository.save(newMovie);

			return mapper.toDto(newMovie);
		} else {
			throw new CinemaException(ErrorCode.INTERNAL_SERVER_ERROR, "Not enough information");
		}

	}

	@Transactional
	@Override
	public MovieDto update(MovieInputDto movieInput) {
		Optional<Movie> movieOptional = movieRepository.findById(movieInput.getId());
		if(movieOptional.isEmpty()) {
			throw new CinemaException(ErrorCode.NOT_FOUND, "movie not found");
		}
		Movie updateMovie = movieOptional.get();

		if(!updateMovie.getName().equals(movieInput.getName())
				&& Objects.nonNull(movieInput.getName())) {
			updateMovie.setName(movieInput.getName());
		}

		if(!updateMovie.getReleaseDate().equals(movieInput.getReleaseDate())
				&& Objects.nonNull(movieInput.getReleaseDate())) {
			updateMovie.setReleaseDate(movieInput.getReleaseDate());
		}

		if(!updateMovie.getCost().equals(movieInput.getCost())
				&& Objects.nonNull(movieInput.getCost())) {
			updateMovie.setCost(movieInput.getCost());
		}

		return mapper.toDto(updateMovie);
	}

}
