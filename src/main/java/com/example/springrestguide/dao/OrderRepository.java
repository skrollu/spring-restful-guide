package com.example.springrestguide.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springrestguide.jpa.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}