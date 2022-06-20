package com.stik.cinema.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stik.cinema.dto.MovieInputDto;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class MovieControllerTest {
	private static final String ID = "00000000-0000-0000-0000-000000000000";
	private static final String NAME = "test";
	private static final LocalDate RELEASE_DATE = LocalDate.of(2022, 6, 19);
	private static final Integer COST = 1;
	private static final String HOME_URL = "/api/movie";
	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	static {
		JSON_MAPPER.registerModule(new JavaTimeModule());
	}


	@Autowired
	private MockMvc mockMvc;

	@Test
	@Sql(value = "classpath:sql/create_movie.sql", executionPhase =
			Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	void findByIdTest() throws Exception {
		mockMvc.perform(get(HOME_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(ID)));
	}

	@Test
	@Sql(value = "classpath:sql/create_movie.sql", executionPhase =
			Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	void searchMoviesTest() throws Exception {
		String content = JSON_MAPPER.writeValueAsString(createMovieInputDto());

		mockMvc.perform(post(HOME_URL + "/find?page=0&size=1").content(content)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].name", equalTo(NAME)));
	}

	@Test
	void createTest() throws Exception {
		String content = JSON_MAPPER.writeValueAsString(createMovieInputDto());

		mockMvc.perform(post(HOME_URL).content(content)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", equalTo(NAME)));
	}

	@Test
	@Sql(value = "classpath:sql/create_movie.sql", executionPhase =
			Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	void updateTest() throws Exception {
		String content = JSON_MAPPER.writeValueAsString(createMovieInputDto());

		mockMvc.perform(patch(HOME_URL).content(content)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", equalTo(NAME)));
	}

	@Test
	@Sql(value = "classpath:sql/create_movie.sql", executionPhase =
			Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	void deleteTest() throws Exception {
		mockMvc.perform(delete(HOME_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", equalTo("ok")));
	}

	private MovieInputDto createMovieInputDto() {
		MovieInputDto movieInputDto = new MovieInputDto();
		movieInputDto.setId(UUID.fromString(ID));
		movieInputDto.setName(NAME);
		movieInputDto.setReleaseDate(RELEASE_DATE);
		movieInputDto.setCost(COST);
		return movieInputDto;
	}
}
