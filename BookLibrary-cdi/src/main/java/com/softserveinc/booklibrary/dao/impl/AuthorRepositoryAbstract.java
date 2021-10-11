package com.softserveinc.booklibrary.dao.impl;

import com.softserveinc.booklibrary.dao.AuthorRepository;
import com.softserveinc.booklibrary.entity.Author;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorRepositoryAbstract extends AbstractEntityRepository<Author> implements AuthorRepository {

	{
		type = Author.class;
	}

}
