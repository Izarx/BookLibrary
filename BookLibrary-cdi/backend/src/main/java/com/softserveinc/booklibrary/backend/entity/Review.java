package com.softserveinc.booklibrary.backend.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "reviews")
public class Review implements AbstractEntity<Integer> {

	public static final int COMMENTER_NAME_LENGTH = 256;
	private static final long serialVersionUID = -7060539758822800075L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id", nullable = false)
	private Integer reviewId;

	@Column(name = "commenter_name", nullable = false, length = COMMENTER_NAME_LENGTH)
	private String commenterName;

	// no need in length, because in base there is no limit for this column
	@Column(name = "comment")
	private String comment;

	@Column(name = "rating", nullable = false)
	private Integer rating;

	@Column(name = "create_date", updatable = false, insertable = false)
	private LocalDateTime createDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;

	@Override
	public Integer getEntityId() {
		return reviewId;
	}
}
