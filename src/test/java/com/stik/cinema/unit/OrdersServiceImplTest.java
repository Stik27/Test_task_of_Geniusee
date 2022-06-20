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

import com.stik.cinema.dto.OrdersDto;
import com.stik.cinema.dto.OrdersInputDto;
import com.stik.cinema.exception.CinemaException;
import com.stik.cinema.exception.ErrorType;
import com.stik.cinema.persistance.Orders;
import com.stik.cinema.repository.MovieRepository;
import com.stik.cinema.repository.OrdersRepository;
import com.stik.cinema.service.OrdersServiceImpl;


@RunWith(SpringRunner.class)
@SpringBootTest
class OrdersServiceImplTest {
	@Autowired
	private OrdersServiceImpl ordersService;
	@MockBean
	private OrdersRepository ordersRepository;
	@MockBean
	private MovieRepository movieRepository;

	@Test
	void findByIdTest() {
		Orders orders = createOrders();
		when(ordersRepository.findById(orders.getId())).thenReturn(Optional.of(orders));

		OrdersDto result = ordersService.findById(orders.getId());

		verify(ordersRepository).findById(orders.getId());
		assertThat(result.getId()).isEqualTo(orders.getId());
		assertThat(result.getMovieId()).isEqualTo(orders.getMovieId());
	}

	@Test
	void deleteTest() {
		Orders orders = createOrders();
		when(ordersRepository.existsById(orders.getId())).thenReturn(true);

		ordersService.delete(orders.getId());

		verify(ordersRepository).deleteById(orders.getId());

	}

	@Test
	void deleteIfEmptyTest() {
		Orders orders = createOrders();

		when(ordersRepository.findById(orders.getId())).thenReturn(Optional.empty());
		UUID id = orders.getId();
		assertThatThrownBy(() -> ordersService.delete(id))
				.isInstanceOf(CinemaException.class)
				.matches((error) -> ((CinemaException) error).getErrorType() == ErrorType.NOT_FOUND);


	}

	@Test
	void createTest() {
		Orders orders = createOrders();
		OrdersInputDto ordersInputDto = createOrdersInputDto(orders);

		when(ordersRepository.save((any(Orders.class)))).thenReturn(orders);
		when(movieRepository.existsById(ordersInputDto.getMovieId())).thenReturn(true);

		OrdersDto result = ordersService.create(ordersInputDto);
		verify(ordersRepository).save(any(Orders.class));
		assertThat(result.getMovieId()).isEqualTo(orders.getMovieId());
	}

	@Test
	void createIfFieldsNullTest() {
		OrdersInputDto orders = new OrdersInputDto();

		assertThatThrownBy(() -> ordersService.create(orders))
				.isInstanceOf(CinemaException.class)
				.matches((error) -> ((CinemaException) error).getErrorType() == ErrorType.INTERNAL_SERVER_ERROR);
	}

	@Test
	void updateTest() {
		Orders orders = createOrders();
		OrdersInputDto ordersInputDto = createOrdersInputDto(orders);
		Orders returnOrders = new Orders();

		returnOrders.setId(orders.getId());
		returnOrders.setMovieId(UUID.randomUUID());
		returnOrders.setOrderTime(LocalDate.now().minusDays(1));
		returnOrders.setParticipants(111);

		when(ordersRepository.findById(ordersInputDto.getId())).thenReturn(Optional.of(returnOrders));
		when(movieRepository.existsById(ordersInputDto.getMovieId())).thenReturn(true);
		when(ordersRepository.save((any(Orders.class)))).thenReturn(returnOrders);
		OrdersDto result = ordersService.update(ordersInputDto);

		verify(ordersRepository).findById(ordersInputDto.getId());
		assertThat(result.getId()).isEqualTo(orders.getId());
		assertThat(result.getMovieId()).isEqualTo(orders.getMovieId());
		assertThat(result.getOrderTime()).isEqualTo(orders.getOrderTime());
		assertThat(result.getParticipants()).isEqualTo(orders.getParticipants());
	}

	@Test
	void updateIfEmptyTest() {
		Orders orders = createOrders();
		OrdersInputDto ordersInputDto = createOrdersInputDto(orders);

		when(ordersRepository.findById(ordersInputDto.getId())).thenReturn(Optional.empty());
		assertThatThrownBy(() -> ordersService.update(ordersInputDto))
				.isInstanceOf(CinemaException.class)
				.matches((error) -> ((CinemaException) error).getErrorType() == ErrorType.NOT_FOUND);
	}

	@Test
	void updateIfMovieNotExistTest() {
		Orders orders = createOrders();
		OrdersInputDto ordersInputDto = createOrdersInputDto(orders);
		ordersInputDto.setMovieId(UUID.randomUUID());

		when(ordersRepository.findById(ordersInputDto.getId())).thenReturn(Optional.of(orders));
		when(movieRepository.findById(ordersInputDto.getMovieId())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> ordersService.update(ordersInputDto))
				.isInstanceOf(CinemaException.class)
				.matches((error) -> ((CinemaException) error).getErrorType() == ErrorType.NOT_FOUND);
	}

	@Test
	void updateIfParticipantsLessTest() {
		Orders orders = createOrders();
		OrdersInputDto ordersInputDto = createOrdersInputDto(orders);
		ordersInputDto.setParticipants(0);

		when(ordersRepository.findById(ordersInputDto.getId())).thenReturn(Optional.of(orders));
		when(movieRepository.existsById(ordersInputDto.getMovieId())).thenReturn(true);
		assertThatThrownBy(() -> ordersService.update(ordersInputDto))
				.isInstanceOf(CinemaException.class)
				.matches((error) -> ((CinemaException) error).getErrorType() == ErrorType.INTERNAL_SERVER_ERROR);
	}

	@Test
	void searchOrdersTest() {
		Orders orders = createOrders();
		OrdersInputDto ordersInputDto = createOrdersInputDto(orders);
		PageRequest pageRequest = PageRequest.of(0, 1);

		when(ordersRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(orders)));

		Page<OrdersDto> page = ordersService.searchOrders(ordersInputDto, pageRequest);

		verify(ordersRepository).findAll(any(Specification.class), any(Pageable.class));
		Optional<OrdersDto> first = page.get().findFirst();
		assertThat(first).isPresent();
		assertThat(first.get().getId()).isEqualTo(orders.getId());
	}

	private Orders createOrders() {
		Orders orders = new Orders();
		orders.setId(UUID.randomUUID());
		orders.setMovieId(UUID.randomUUID());
		orders.setOrderTime(LocalDate.now());
		orders.setParticipants(2);
		return orders;
	}

	private OrdersInputDto createOrdersInputDto(Orders orders) {
		OrdersInputDto result = new OrdersInputDto();
		result.setId(orders.getId());
		result.setMovieId((orders.getMovieId()));
		result.setOrderTime(orders.getOrderTime());
		result.setParticipants(orders.getParticipants());
		return result;
	}
}