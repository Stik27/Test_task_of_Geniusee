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
import com.stik.cinema.dto.OrdersInputDto;

@AutoConfigureMockMvc
@Sql(value = "classpath:sql/create_movie.sql", executionPhase =
		Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
@SpringBootTest
class OrdersControllerTest {
	private static final String ID = "00000000-0000-0000-0000-000000000011";

	private static final String MOVIE_ID = "00000000-0000-0000-0000-000000000000";
	private static final String ORDER_TIME = "2022-06-20";

	private static final Integer PARTICIPANTS = 3;
	private static final String HOME_URL = "/api/orders";
	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	static {
		JSON_MAPPER.registerModule(new JavaTimeModule());
	}


	@Autowired
	private MockMvc mockMvc;

	@Test
	@Sql(value = "classpath:sql/create_movie.sql", executionPhase =
			Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "classpath:sql/create_orders.sql", executionPhase =
			Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	void findByIdTest() throws Exception {
		mockMvc.perform(get(HOME_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(ID)));
	}

	@Test
	@Sql(value = "classpath:sql/create_movie.sql", executionPhase =
			Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "classpath:sql/create_orders.sql", executionPhase =
			Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	void searchOrderssTest() throws Exception {
		String content = JSON_MAPPER.writeValueAsString(createOrdersInputDto());

		mockMvc.perform(post(HOME_URL + "/find?page=0&size=1").content(content)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id", equalTo(ID)));
	}

	@Test
	@Sql(value = "classpath:sql/create_movie.sql", executionPhase =
			Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	void createTest() throws Exception {
		String content = JSON_MAPPER.writeValueAsString(createOrdersInputDto());

		mockMvc.perform(post(HOME_URL).content(content)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.orderTime", equalTo(ORDER_TIME)));
	}

	@Test
	@Sql(value = "classpath:sql/create_movie.sql", executionPhase =
			Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "classpath:sql/create_orders.sql", executionPhase =
			Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	void updateTest() throws Exception {
		String content = JSON_MAPPER.writeValueAsString(createOrdersInputDto());

		mockMvc.perform(patch(HOME_URL).content(content)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", equalTo(ID)));
	}

	@Test
	@Sql(value = "classpath:sql/create_movie.sql", executionPhase =
			Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "classpath:sql/create_orders.sql", executionPhase =
			Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	void deleteTest() throws Exception {
		mockMvc.perform(delete(HOME_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", equalTo("ok")));
	}

	private OrdersInputDto createOrdersInputDto() {
		OrdersInputDto OrdersInputDto = new OrdersInputDto();
		OrdersInputDto.setId(UUID.fromString(ID));
		OrdersInputDto.setMovieId(UUID.fromString(MOVIE_ID));
		OrdersInputDto.setOrderTime(LocalDate.parse(ORDER_TIME));
		OrdersInputDto.setParticipants(PARTICIPANTS);
		return OrdersInputDto;
	}
}

