package com.stik.cinema.specification;

import static org.springframework.data.jpa.domain.Specification.where;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.stik.cinema.dto.OrdersInputDto;
import com.stik.cinema.persistance.Orders;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrdersSpecifications {

	public static Specification<Orders> orders(OrdersInputDto ordersInputDto) {
		return where(OrdersSpecifications.equalMovieId(ordersInputDto.getMovieId()))
				.and(OrdersSpecifications.equalOrderTime(ordersInputDto.getOrderTime()))
				.and(OrdersSpecifications.equalParticipants(ordersInputDto.getParticipants()));
	}

	private static Specification<Orders> equalMovieId(UUID movieId) {
		if(movieId == null) {
			return null;
		}
		return (root, query, cb) -> cb.equal(root.get("movieId"), movieId);
	}

	private static Specification<Orders> equalOrderTime(LocalDate orderTime) {
		if(orderTime == null) {
			return null;
		}
		return (root, query, cb) -> cb.equal(root.get("orderTime"), orderTime);
	}

	private static Specification<Orders> equalParticipants(Integer participants) {
		if(participants == null) {
			return null;
		}
		return (root, query, cb) -> cb.equal(root.get("participants"), participants);
	}
}
