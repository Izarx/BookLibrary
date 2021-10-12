package com.softserveinc.booklibrary.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@Table(name = "authors")
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "author_id", nullable = false)
	private Integer authorId;

	//TODO length?
	@Column(name = "first_name", nullable = false)
	private String firstName;

	//TODO length?
	@Column(name = "last_name")
	private String lastName;

	@CreationTimestamp
	//TODO wrong declaration
	@Column(name = "create_date")
	private LocalDateTime createDate;

	@ManyToMany
	@JoinTable(
			name = "authors_books",
			joinColumns = {@JoinColumn(name = "author_id")},
			inverseJoinColumns = {@JoinColumn(name = "book_id")}
	)
	private Set<Book> books;
}