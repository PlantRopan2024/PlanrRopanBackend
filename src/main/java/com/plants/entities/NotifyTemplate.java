package com.plants.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notify_template")
public class NotifyTemplate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int primarykey;
	
	@Column(unique = true)
	private String templateId;
	private String title;
    @Column(name = "message_text", columnDefinition = "TEXT")
	private String messageText;
    private boolean isActive;
    
    @Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	    
	@Column(name = "updated_at", nullable = false, updatable = true)
	private LocalDateTime updatedAt;

	public NotifyTemplate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NotifyTemplate(int primarykey, String templateId, String title, String messageText, boolean isActive, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super();
		this.primarykey = primarykey;
		this.templateId = templateId;
		this.title = title;
		this.messageText = messageText;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getPrimarykey() {
		return primarykey;
	}

	public void setPrimarykey(int primarykey) {
		this.primarykey = primarykey;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
