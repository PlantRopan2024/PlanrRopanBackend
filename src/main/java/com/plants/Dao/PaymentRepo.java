package com.plants.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plants.entities.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {

}
