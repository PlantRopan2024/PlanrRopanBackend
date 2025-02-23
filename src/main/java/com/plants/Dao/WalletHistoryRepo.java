package com.plants.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plants.entities.WalletHistory;

public interface WalletHistoryRepo extends JpaRepository<WalletHistory, Integer>{

}
