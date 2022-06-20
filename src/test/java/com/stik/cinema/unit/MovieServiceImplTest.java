package com.stik.cinema.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import com.stik.cinema.dto.MovieDto;
import com.stik.cinema.dto.MovieInputDto;
import com.stik.cinema.exception.CinemaException;
import com.stik.cinema.exception.ErrorType;
import com.stik.cinema.persistance.Movie;
import com.stik.cinema.repository.MovieRepository;
import com.stik.cinema.service.MovieServiceImpl;


@RunWith(SpringRunner.class)
@SpringBootTest
class MovieServiceImplTest {

	@Autowired
	private MovieServiceImpl movieService;

	@MockBean
	private MovieRepository movieRepository;

	@Test
	void findByIdTest() {
		Movie movie = createMovie();
		when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));

		MovieDto result = movieService.findById(movie.getId());

		verify(movieRepository).findById(movie.getId());
		assertThat(result.getId()).isEqualTo(movie.getId());
		assertThat(result.getName()).isEqualTo(movie.getName());
	}

	@Test
	void deleteTest() {
		Movie movie = createMovie();
		when(movieRepository.existsById(movie.getId())).thenReturn(true);

		movieService.delete(movie.getId());

		verify(movieRepository).deleteById(movie.getId());
	}

	@Test
	void deleteIfEmptyTest() {
		Movie movie = createMovie();
		when(movieRepository.findById(movie.getId())).thenReturn(Optional.empty());
		UUID id = movie.getId();
		assertThatThrownBy(() -> movieService.delete(id))
				.isInstanceOf(CinemaException.class)
				.matches((error) -> ((CinemaException) error).getErrorType() == ErrorType.NOT_FOUND);
	}

	@Test
	void createTest() {
		Movie movie = createMovie();
		MovieInputDto movieInputDto = createMovieInputDto(movie);
		when(movieRepository.save((any(Movie.class)))).thenReturn(movie);
		MovieDto result = movieService.create(movieInputDto);
		verify(movieRepository).save(any(Movie.class));
		assertThat(result.getName()).isEqualTo(movie.getName());
	}

	@Test
	void createIfFieldsNullTest() {
		MovieInputDto movie = new MovieInputDto();
		movie.setName("test");

		assertThatThrownBy(() -> movieService.create(movie))
				.isInstanceOf(CinemaException.class)
				.matches((error) -> ((CinemaException) error).getErrorType() == ErrorType.INTERNAL_SERVER_ERROR);
	}

	@Test
	void updateTest() {
		Movie movie = createMovie();
		MovieInputDto movieInputDto = createMovieInputDto(movie);
		Movie returnMovie = new Movie();

		returnMovie.setId(movie.getId());
		returnMovie.setName("movieInput");
		returnMovie.setReleaseDate(LocalDate.now().minusDays(1));
		returnMovie.setCost(111);

		when(movieRepository.findById(movieInputDto.getId())).thenReturn(Optional.of(returnMovie));
		when(movieRepository.save((any(Movie.class)))).thenReturn(returnMovie);
		MovieDto result = movieService.update(movieInputDto);

		verify(movieRepository).findById(movieInputDto.getId());
		assertThat(result.getId()).isEqualTo(movie.getId());
		assertThat(result.getName()).isEqualTo(movie.getName());
		assertThat(result.getReleaseDate()).isEqualTo(movie.getReleaseDate());
		assertThat(result.getCost()).isEqualTo(movie.getCost());
	}

	@Test
	void updateIfEmptyTest() {
		Movie movie = createMovie();
		MovieInputDto movieInputDto = createMovieInputDto(movie);

		when(movieRepository.findById(movieInputDto.getId())).thenReturn(Optional.empty());
		assertThatThrownBy(() -> movieService.update(movieInputDto))
				.isInstanceOf(CinemaException.class)
				.matches((error) -> ((CinemaException) error).getErrorType() == ErrorType.NOT_FOUND);
	}

	@Test
	void updateIfNameDuplicateTest() {
		Movie movie = createMovie();
		MovieInputDto movieInputDto = createMovieInputDto(movie);
		movieInputDto.setName("test");

		when(movieRepository.findById(movieInputDto.getId())).thenReturn(Optional.of(movie));
		when(movieRepository.existsByName(movieInputDto.getName())).thenReturn(true);

		assertThatThrownBy(() -> movieService.update(movieInputDto))
				.isInstanceOf(CinemaException.class)
				.matches((error) -> ((CinemaException) error).getErrorType() == ErrorType.ALREADY_OCCUPIED);
	}

	@Test
	void updateIfNameDifferentTest() {
		Movie movie = createMovie();
		MovieInputDto movieInputDto = createMovieInputDto(movie);
		movieInputDto.setName("test");
		when(movieRepository.findById(movieInputDto.getId())).thenReturn(Optional.empty());
		assertThatThrownBy(() -> movieService.update(movieInputDto))
				.isInstanceOf(CinemaException.class)
				.matches((error) -> ((CinemaException) error).getErrorType() == ErrorType.NOT_FOUND);
	}

	@Test
	void updateIfCostLessTest() {
		Movie movie = createMovie();
		MovieInputDto movieInputDto = createMovieInputDto(movie);
		movieInputDto.setCost(0);
		when(movieRepository.findById(movieInputDto.getId())).thenReturn(Optional.of(movie));
		assertThatThrownBy(() -> movieService.update(movieInputDto))
				.isInstanceOf(CinemaException.class)
				.matches((error) -> ((CinemaException) error).getErrorType() == ErrorType.INTERNAL_SERVER_ERROR);
	}

	@Test
	void searchMoviesTest() {
		Movie movie = createMovie();
		MovieInputDto movieInputDto = createMovieInputDto(movie);
		PageRequest pageRequest = PageRequest.of(0, 1);
		when(movieRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(movie)));
		Page<MovieDto> page = movieService.searchMovies(movieInputDto, pageRequest);
		verify(movieRepository).findAll(any(Specification.class), any(Pageable.class));
		Optional<MovieDto> first = page.get().findFirst();
		assertThat(first).isPresent();
		assertThat(first.get().getName()).isEqualTo(movie.getName());
	}

	private Movie createMovie() {
		Movie movie = new Movie();
		movie.setId(UUID.randomUUID());
		movie.setName("name");
		movie.setReleaseDate(LocalDate.now());
		movie.setCost(5);
		return movie;
	}

	private MovieInputDto createMovieInputDto(Movie movie) {
		MovieInputDto result = new MovieInputDto();
		result.setId(movie.getId());
		result.setName(movie.getName());
		result.setReleaseDate(movie.getReleaseDate());
		result.setCost(movie.getCost());
		return result;
	}
}