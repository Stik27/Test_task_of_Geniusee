package com.stik.cinema.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class MovieInputDto {
	private UUID id;
	private String name;
	private LocalDate releaseDate;
	private Integer cost;
}
