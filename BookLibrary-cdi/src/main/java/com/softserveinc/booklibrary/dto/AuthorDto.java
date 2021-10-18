package com.softserveinc.booklibrary.dto;

import java.time.LocalDateTime;

import com.softserveinc.booklibrary.entity.Author;
import lombok.Getter;

@Getter
public final class AuthorDto {

	private final Integer authorId;
	private final String firstName;
	private final String lastName;
	private final LocalDateTime creationDate;

	public AuthorDto(Author author) {
		authorId = author.getAuthorId();
		firstName = author.getFirstName();
		lastName = author.getLastName();
		creationDate = author.getCreateDate();
	}

	public Author convertToAuthor() {
		Author author = new Author();
		author.setAuthorId(authorId);
		author.setFirstName(firstName);
		author.setLastName(lastName);
		return author;
	}
}
