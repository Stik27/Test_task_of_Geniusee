package com.stik.cinema.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stik.cinema.dto.MovieDto;
import com.stik.cinema.dto.MovieInputDto;
import com.stik.cinema.exception.CinemaException;
import com.stik.cinema.exception.ErrorType;
import com.stik.cinema.mapper.MovieMapper;
import com.stik.cinema.persistance.Movie;
import com.stik.cinema.repository.MovieRepository;
import com.stik.cinema.specification.MovieSpecifications;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {
	private final MovieRepository movieRepository;
	private final MovieMapper mapper;

	@Override
	@Transactional(readOnly = true)
	public MovieDto findById(UUID id) {
		Movie byId = movieRepository.findById(id)
				.orElseThrow(() -> new CinemaException(ErrorType.NOT_FOUND, "This movie doesn't found"));
		return mapper.toDto(byId);
	}

	@Override
	public Page<MovieDto> searchMovies(MovieInputDto movieInputDto, Pageable pageable) {
		Page<Movie> page = movieRepository.findAll(MovieSpecifications.movieSpecification(movieInputDto), pageable);
		List<MovieDto> movieDto = page.get().map(mapper::toDto).toList();
		return new PageImpl<>(movieDto);
	}

	@Override
	public MovieDto create(MovieInputDto movieInput) {

		checkForDuplicate(movieInput.getName());
		if(Objects.isNull(movieInput.getName())
				&& Objects.isNull(movieInput.getReleaseDate())
				&& Objects.isNull(movieInput.getCost())) {
			throw new CinemaException(ErrorType.INTERNAL_SERVER_ERROR, "There is not enough information");
		}
		checkCost(movieInput.getCost());
		Movie newMovie = new Movie();
		newMovie.setName(movieInput.getName());
		newMovie.setReleaseDate(movieInput.getReleaseDate());
		newMovie.setCost(movieInput.getCost());

		Movie save = movieRepository.save(newMovie);

		return mapper.toDto(save);
	}

	@Override
	public MovieDto update(MovieInputDto movieInput) {

		Movie updatedMovie = movieRepository.findById(movieInput.getId())
				.orElseThrow(() -> new CinemaException(ErrorType.NOT_FOUND, "This movie doesn't found"));

		if(!Objects.equals(movieInput.getName(), updatedMovie.getName()) && Objects.nonNull(movieInput.getName())) {
			checkForDuplicate(movieInput.getName());
			updatedMovie.setName(movieInput.getName());
		}

		if(Objects.nonNull(movieInput.getReleaseDate())) {
			updatedMovie.setReleaseDate(movieInput.getReleaseDate());
		}

		if(Objects.nonNull(movieInput.getCost())) {
			checkCost(movieInput.getCost());
			updatedMovie.setCost(movieInput.getCost());
		}
		return mapper.toDto(updatedMovie);
	}

	@Override
	public void delete(UUID id) {
		if(!movieRepository.existsById(id)) {
			throw new CinemaException(ErrorType.NOT_FOUND, "This movie doesn't exists");
		}
		movieRepository.deleteById(id);
	}

	private void checkForDuplicate(String name) {
		if(movieRepository.existsByName(name)) {
			throw new CinemaException(ErrorType.ALREADY_OCCUPIED, "Such a name is already occupied");
		}
	}

	private void checkCost(int cost) {
		if(cost <= 0) {
			throw new CinemaException(ErrorType.INTERNAL_SERVER_ERROR, "The cost must be greater than 0");
		}
	}
}
