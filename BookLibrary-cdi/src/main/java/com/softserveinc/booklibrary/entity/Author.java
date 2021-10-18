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
public class Author implements EntityLibrary<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "author_id", nullable = false)
	private Integer authorId;

	@Column(name = "first_name", nullable = false, length = 512)
	private String firstName;

	@Column(name = "last_name", length = 512)
	private String lastName;

	@CreationTimestamp  // date created by creating instance forbidden for update and insert
	@Column(name = "create_date")
	private LocalDateTime createDate;

	@ManyToMany
	@JoinTable(
			name = "authors_books",
			joinColumns = {@JoinColumn(name = "author_id")},
			inverseJoinColumns = {@JoinColumn(name = "book_id")}
	)
	private Set<Book> books;

	@Override
	public Integer getEntityId() {
		return authorId;
	}
}