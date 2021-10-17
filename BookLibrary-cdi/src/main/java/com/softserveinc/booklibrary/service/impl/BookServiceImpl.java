package com.softserveinc.booklibrary.service.impl;

import com.softserveinc.booklibrary.dao.BookRepository;
import com.softserveinc.booklibrary.entity.Book;
import com.softserveinc.booklibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl extends AbstractEntityService<Book> implements BookService {

	@Autowired
	public BookServiceImpl(BookRepository bookRepository) {
		repository = bookRepository;
	}

	@Override
	public Book create(Book entity) {
		return null;
	}

	@Override
	public Book update(Book entity) {
		return null;
	}
}
