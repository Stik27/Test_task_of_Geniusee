package com.stik.cinema.persistance;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false, unique = true)
	private UUID id;

	@Column(name = "movie_id", nullable = false)
	private UUID movieId;

	@Column(name = "order_time", nullable = false)
	private LocalDateTime orderTime;

	@Column(name = "participants", nullable = false)
	private Integer participants;

}
