package com.softserveinc.booklibrary.dto;

import com.softserveinc.booklibrary.entity.Author;
import lombok.Getter;

@Getter
public final class AuthorDto {

	private final Integer authorId;
	private final String firstName;
	private final String lastName;

	public AuthorDto(Author author) {
		authorId = author.getAuthorId();
		firstName = author.getFirstName();
		lastName = author.getLastName();
	}

	public Author convertToAuthor() {
		Author author = new Author();
		author.setAuthorId(authorId);
		author.setFirstName(firstName);
		author.setLastName(lastName);
		return author;
	}
}
