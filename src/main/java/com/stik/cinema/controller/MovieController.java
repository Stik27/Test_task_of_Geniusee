package com.stik.cinema.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stik.cinema.dto.MovieDto;
import com.stik.cinema.dto.MovieInputDto;
import com.stik.cinema.service.MovieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
public class MovieController {
	private final MovieService movieService;

	@GetMapping("/{id}")
	public MovieDto findById(@PathVariable UUID id) {
		return movieService.findById(id);
	}

	@PostMapping("/find")
	public Page<MovieDto> findAll(@RequestBody(required = false) MovieInputDto movieInputDto,
	                              @RequestParam("page") Integer page,
	                              @RequestParam("size") Integer size) {
		return movieService.searchMovies(movieInputDto, PageRequest.of(page, size));
	}

	@PostMapping
	public MovieDto create(@RequestBody MovieInputDto moveInput) {
		return movieService.create(moveInput);
	}

	@PatchMapping
	public MovieDto update(@RequestBody MovieInputDto moveInput) {
		return movieService.update(moveInput);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable UUID id) {
		movieService.delete(id);
		return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("ok");
	}
}
