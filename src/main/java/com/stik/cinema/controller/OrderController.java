package com.stik.cinema.controller;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stik.cinema.dto.OrdersDto;
import com.stik.cinema.dto.OrdersInputDto;
import com.stik.cinema.service.OrdersService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
	private final OrdersService ordersService;

	@GetMapping("/{id}")
	public OrdersDto findById(@PathVariable UUID id) {
		return ordersService.findById(id);
	}

	@PostMapping
	public OrdersDto create(@RequestBody OrdersInputDto ordersInput) {
		return ordersService.create(ordersInput);
	}

	@PatchMapping
	public OrdersDto update(@RequestBody OrdersInputDto ordersInput) {
		return ordersService.update(ordersInput);
	}

	@DeleteMapping("/{id}")
	public Map<String, String> delete(@PathVariable UUID id) {
		ordersService.delete(id);
		return Collections.singletonMap("response", "deletion was successful");
	}
}



