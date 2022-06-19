package com.stik.cinema.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.stik.cinema.dto.OrdersDto;
import com.stik.cinema.dto.OrdersInputDto;

@Service
public interface OrdersService {
	OrdersDto findById(UUID id);

	OrdersDto create(OrdersInputDto movieInput);

	OrdersDto update(OrdersInputDto movieInput);

	void delete(UUID id);


}
