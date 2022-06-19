package com.stik.cinema.criteria;

import java.util.function.Consumer;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.stik.cinema.persistance.Movie;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovieSearchQueryCriteriaConsumer implements Consumer<SearchCriteria> {

	private Predicate predicate;
	private CriteriaBuilder builder;
	private Root<Movie> r;

	@Override
	public void accept(SearchCriteria param) {
		if(param.getOperation().equalsIgnoreCase(">")) {
			predicate = builder.and(predicate, builder.greaterThanOrEqualTo(
					r.get(param.getKey()), String.valueOf(Integer.parseInt((String) param.getValue()) - 1)));
		} else if(param.getOperation().equalsIgnoreCase("<")) {
			predicate = builder.and(predicate, builder.lessThanOrEqualTo(
					r.get(param.getKey()), String.valueOf(Integer.parseInt((String) param.getValue()) - 1)));
		} else if(param.getOperation().equalsIgnoreCase(":")) {
			if(r.get(param.getKey()).getJavaType() == String.class) {
				predicate = builder.and(predicate, builder.like(
						r.get(param.getKey()), "%" + param.getValue() + "%"));
			} else {
				predicate = builder.and(predicate, builder.equal(
						r.get(param.getKey()), param.getValue()));
			}
		}
	}
}
