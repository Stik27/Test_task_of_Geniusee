package com.stik.cinema.service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stik.cinema.dto.OrdersDto;
import com.stik.cinema.dto.OrdersInputDto;
import com.stik.cinema.exception.CinemaException;
import com.stik.cinema.exception.ErrorCode;
import com.stik.cinema.mapper.OrdersMapper;
import com.stik.cinema.persistance.Orders;
import com.stik.cinema.repository.MovieRepository;
import com.stik.cinema.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrdersServiceImpl implements OrdersService {
	private final OrdersRepository ordersRepository;
	private final MovieRepository movieRepository;

	private final OrdersMapper mapper;

	@Override
	public OrdersDto findById(UUID id) {
		Orders byId = ordersRepository.findById(id)
				.orElseThrow(() -> new CinemaException(ErrorCode.NOT_FOUND, "order not found"));
		return mapper.toDto(byId);
	}

	@Transactional
	@Override
	public OrdersDto create(OrdersInputDto ordersInput) {
		if(Objects.nonNull(ordersInput.getMovieId())
				&& Objects.nonNull(ordersInput.getParticipants())) {
			if(movieRepository.findById(ordersInput.getMovieId()).isEmpty()) {
				throw new CinemaException(ErrorCode.NOT_FOUND, "this movie not exists");
			}
			Orders newOrders = new Orders();
			newOrders.setId(UUID.randomUUID());
			newOrders.setMovieId(ordersInput.getMovieId());
			newOrders.setOrderTime(LocalDateTime.now());
			newOrders.setParticipants(ordersInput.getParticipants());

			ordersRepository.save(newOrders);

			return mapper.toDto(newOrders);
		} else {
			throw new CinemaException(ErrorCode.INTERNAL_SERVER_ERROR, "Not enough information");
		}
	}

	@Transactional
	@Override
	public OrdersDto update(OrdersInputDto movieInput) {

		Optional<Orders> ordersOptional = ordersRepository.findById(movieInput.getId());
		if(ordersOptional.isEmpty()) {
			throw new CinemaException(ErrorCode.NOT_FOUND, "orders not found");
		}
		Orders updateOrders = ordersOptional.get();

		if(!updateOrders.getMovieId().equals(movieInput.getMovieId())
				&& Objects.nonNull(movieInput.getMovieId())) {
			updateOrders.setMovieId(movieInput.getMovieId());
		}

		if(!updateOrders.getOrderTime().equals(movieInput.getOrderTime())
				&& Objects.nonNull(movieInput.getOrderTime())) {
			updateOrders.setOrderTime(movieInput.getOrderTime());
		}

		if(!updateOrders.getParticipants().equals(movieInput.getParticipants())
				&& Objects.nonNull(movieInput.getParticipants())) {
			updateOrders.setParticipants(movieInput.getParticipants());
		}
		return mapper.toDto(updateOrders);
	}

	@Override
	public void deleteOrder(UUID id) {
		ordersRepository.deleteById(id);
	}

	@Override
	public void deleteAllByMovieId(UUID id) {
		ordersRepository.deleteAllByMovieId(id);
	}
}
