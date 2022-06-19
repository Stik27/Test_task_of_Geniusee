package com.stik.cinema.persistance;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Hibernate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Orders that = (Orders) o;
		return id != null && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}


}
