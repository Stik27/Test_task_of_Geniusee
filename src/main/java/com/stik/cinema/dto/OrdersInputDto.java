package com.stik.cinema.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class OrdersInputDto {
	private UUID id;
	private UUID movieId;
	private LocalDate orderTime;
	private Integer participants;
}
