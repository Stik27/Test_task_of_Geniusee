package com.stik.cinema.persistance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Hibernate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "movie")
public class Movie {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false, unique = true)
	private UUID id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "release_date", nullable = false)
	private LocalDateTime releaseDate;

	@Column(name = "cost", nullable = false)
	private Integer cost;

	@OneToMany(mappedBy = "movieId", cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	private List<Orders> orders;

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Movie that = (Movie) o;
		return id != null && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}


}
