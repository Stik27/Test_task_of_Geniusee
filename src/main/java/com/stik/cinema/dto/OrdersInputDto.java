package com.stik.cinema.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class OrdersInputDto {
	private UUID id;
	private UUID movieId;
	private LocalDateTime orderTime;
	private Integer participants;
}
