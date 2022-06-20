package com.stik.cinema.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stik.cinema.dto.OrdersDto;
import com.stik.cinema.dto.OrdersInputDto;
import com.stik.cinema.exception.CinemaException;
import com.stik.cinema.exception.ErrorType;
import com.stik.cinema.mapper.OrdersMapper;
import com.stik.cinema.persistance.Orders;
import com.stik.cinema.repository.MovieRepository;
import com.stik.cinema.repository.OrdersRepository;
import com.stik.cinema.specification.OrdersSpecifications;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {
	private final OrdersRepository ordersRepository;
	private final MovieRepository movieRepository;
	private final OrdersMapper mapper;

	@Override
	@Transactional(readOnly = true)
	public OrdersDto findById(UUID id) {
		Orders byId = ordersRepository.findById(id)
				.orElseThrow(() -> new CinemaException(ErrorType.NOT_FOUND, "Order doesn't found"));
		return mapper.toDto(byId);
	}

	@Override
	public Page<OrdersDto> searchOrders(OrdersInputDto ordersInputDto, Pageable pageable) {
		Page<Orders> page = ordersRepository
				.findAll(OrdersSpecifications.orders(ordersInputDto), pageable);
		List<OrdersDto> movieDto = page.get().map(mapper::toDto).toList();
		return new PageImpl<>(movieDto);
	}

	@Override
	public OrdersDto create(OrdersInputDto ordersInput) {
		if(Objects.nonNull(ordersInput.getMovieId())
				&& Objects.nonNull(ordersInput.getParticipants())) {
			checkExistenceMovie(ordersInput.getMovieId());
			checkParticipants(ordersInput.getParticipants());
			Orders newOrders = new Orders();
			newOrders.setMovieId(ordersInput.getMovieId());
			newOrders.setOrderTime(LocalDate.now());
			newOrders.setParticipants(ordersInput.getParticipants());

			ordersRepository.save(newOrders);
			return mapper.toDto(newOrders);
		} else {
			throw new CinemaException(ErrorType.INTERNAL_SERVER_ERROR, "There is not enough information");
		}
	}

	@Override
	public OrdersDto update(OrdersInputDto ordersInput) {
		Orders updatedOrders = ordersRepository.findById(ordersInput.getId())
				.orElseThrow(() -> new CinemaException(ErrorType.NOT_FOUND, "Orders doesn't found"));

		if(Objects.nonNull(ordersInput.getMovieId())) {
			checkExistenceMovie(ordersInput.getMovieId());
			updatedOrders.setMovieId(ordersInput.getMovieId());
		}

		if(Objects.nonNull(ordersInput.getOrderTime())) {
			updatedOrders.setOrderTime(ordersInput.getOrderTime());
		}

		if(Objects.nonNull(ordersInput.getParticipants())) {
			checkParticipants(ordersInput.getParticipants());
			updatedOrders.setParticipants(ordersInput.getParticipants());
		}
		return mapper.toDto(updatedOrders);
	}

	@Override
	public void delete(UUID id) {
		if(!ordersRepository.existsById(id)) {
			throw new CinemaException(ErrorType.NOT_FOUND, "This order doesn't exists");
		}
		ordersRepository.deleteById(id);
	}

	private void checkExistenceMovie(UUID movieId) {
		if(!movieRepository.existsById(movieId)) {
			throw new CinemaException(ErrorType.NOT_FOUND, "This movie doesn't exists");
		}
	}

	private void checkParticipants(int participants) {
		if(participants <= 0) {
			throw new CinemaException(ErrorType.INTERNAL_SERVER_ERROR,
					"The number of participants must be more than 0");
		}
	}
}
