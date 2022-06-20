package com.stik.cinema.mapper;

import org.mapstruct.Mapper;

import com.stik.cinema.dto.OrdersDto;
import com.stik.cinema.persistance.Orders;

@Mapper(componentModel = "spring")
public interface OrdersMapper {
	OrdersDto toDto(Orders orders);
}