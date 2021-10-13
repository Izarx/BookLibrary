package com.softserveinc.booklibrary.dao.impl;

import com.softserveinc.booklibrary.dao.BookRepository;
import com.softserveinc.booklibrary.entity.Book;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl extends AbstractEntityRepository<Book> implements BookRepository {

	@Override
	public Book getById(Integer id) {
		return entityManager.find(Book.class, id);
	}

}
