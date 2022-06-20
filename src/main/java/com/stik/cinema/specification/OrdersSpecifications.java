package com.stik.cinema.specification;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.stik.cinema.persistance.Orders;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrdersSpecifications {
	public static Specification<Orders> equalMovieId(UUID movieId) {
		if(movieId == null) {
			return null;
		}
		return (root, query, cb) -> cb.equal(root.get("movieId"), movieId);
	}

	public static Specification<Orders> equalOrderTime(LocalDate orderTime) {
		if(orderTime == null) {
			return null;
		}
		return (root, query, cb) -> cb.equal(root.get("orderTime"), orderTime);
	}

	public static Specification<Orders> equalParticipants(Integer participants) {
		if(participants == null) {
			return null;
		}
		return (root, query, cb) -> cb.equal(root.get("participants"), participants);
	}
}
