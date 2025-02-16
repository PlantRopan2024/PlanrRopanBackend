package com.plants.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Reviews {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int primaryKey;
	
	private String reviewsName;
	private String rating;
	private String reviewsComment;
	private String likes;
	private String dislikes;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "fk_plans_id")
	private Plans plans;

	public Reviews() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Reviews(int primaryKey, String reviewsName, String rating, String reviewsComment, String likes,
			String dislikes, Plans plans) {
		super();
		this.primaryKey = primaryKey;
		this.reviewsName = reviewsName;
		this.rating = rating;
		this.reviewsComment = reviewsComment;
		this.likes = likes;
		this.dislikes = dislikes;
		this.plans = plans;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getReviewsName() {
		return reviewsName;
	}

	public void setReviewsName(String reviewsName) {
		this.reviewsName = reviewsName;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getReviewsComment() {
		return reviewsComment;
	}

	public void setReviewsComment(String reviewsComment) {
		this.reviewsComment = reviewsComment;
	}

	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	public String getDislikes() {
		return dislikes;
	}

	public void setDislikes(String dislikes) {
		this.dislikes = dislikes;
	}

	public Plans getPlans() {
		return plans;
	}

	public void setPlans(Plans plans) {
		this.plans = plans;
	}

	@Override
	public String toString() {
		return "Reviews [primaryKey=" + primaryKey + ", reviewsName=" + reviewsName + ", rating=" + rating
				+ ", reviewsComment=" + reviewsComment + ", likes=" + likes + ", dislikes=" + dislikes + ", plans=" + plans
				+ "]";
	}
}
