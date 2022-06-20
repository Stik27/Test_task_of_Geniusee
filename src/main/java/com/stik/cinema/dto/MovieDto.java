package com.stik.cinema.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class MovieDto {
	private UUID id;
	private String name;
	private LocalDate releaseDate;
	private Integer cost;
	private List<OrdersDto> orders;

}
