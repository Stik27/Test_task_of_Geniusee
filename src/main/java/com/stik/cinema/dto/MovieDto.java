package com.stik.cinema.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class MovieDto {
	private UUID id;
	private String name;
	private LocalDateTime releaseDate;
	private Integer cost;

}
