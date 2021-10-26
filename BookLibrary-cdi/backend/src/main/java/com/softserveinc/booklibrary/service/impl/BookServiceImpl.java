package com.softserveinc.booklibrary.service.impl;

import com.softserveinc.booklibrary.repository.BookRepository;
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

}
