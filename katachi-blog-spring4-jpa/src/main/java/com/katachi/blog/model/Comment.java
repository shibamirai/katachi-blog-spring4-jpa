package com.katachi.blog.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer postId;

	private Integer userId;

	private String body;

	@Column(name="posted_at", columnDefinition="TIMESTAMP", updatable=false)
	@CreationTimestamp
	private LocalDateTime postedAt;

	@Column(name="updated_at", columnDefinition="TIMESTAMP")
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId", insertable=false, updatable=false)
	private User user;

}
