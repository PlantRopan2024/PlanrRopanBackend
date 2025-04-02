package com.plants.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plants.entities.RejectedOrders;

public interface RejectedOrdersRepo extends JpaRepository<RejectedOrders, Integer> {

}
