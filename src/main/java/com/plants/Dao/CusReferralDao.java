package com.plants.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.AgentReferral;
import com.plants.entities.CusReferral;

public interface CusReferralDao extends JpaRepository<CusReferral, Integer>{
	
	
	@Query("SELECT e FROM CusReferral e WHERE e.referralCode = :referralCode and e.referredCustId = :referredCustId")
	public CusReferral getUseReferralCodeCus(@Param("referralCode") String referralCode , @Param("referredCustId") int referredCustId);
}
