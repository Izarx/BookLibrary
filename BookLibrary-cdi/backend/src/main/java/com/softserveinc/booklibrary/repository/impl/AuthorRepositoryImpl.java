package com.softserveinc.booklibrary.repository.impl;

import com.softserveinc.booklibrary.repository.AuthorRepository;
import com.softserveinc.booklibrary.entity.Author;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorRepositoryImpl extends AbstractEntityRepository<Author> implements AuthorRepository {

	@Override
	public boolean isEntityValid(Author author) {
		String firstName = author.getFirstName();
		String lastName = author.getLastName();
		if (firstName == null || firstName.length() > Author.FIRST_NAME_LENGTH) {
			return false;
		}
		return lastName != null && lastName.length() <= Author.LAST_NAME_LENGTH;
	}
}
