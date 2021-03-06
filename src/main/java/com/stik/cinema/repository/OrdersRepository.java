package com.stik.cinema.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.stik.cinema.persistance.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, UUID>, JpaSpecificationExecutor<Orders> {

}
