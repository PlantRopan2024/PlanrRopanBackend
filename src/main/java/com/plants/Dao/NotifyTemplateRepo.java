package com.plants.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.plants.entities.NotifyTemplate;

public interface NotifyTemplateRepo extends JpaRepository<NotifyTemplate, Integer>{
	
	@Query("SELECT n FROM NotifyTemplate n")
	List<NotifyTemplate> getAllTemplate();
	
	@Query("SELECT n FROM NotifyTemplate n Where n.primarykey = :primarykey")
	NotifyTemplate getNotifyPk(@Param("primarykey") String primarykey);
	
	@Query("SELECT MAX(CAST(SUBSTRING(n.templateId, 17) AS int)) FROM NotifyTemplate n ")
	Integer getMaxSequenceTemplateId();
}
