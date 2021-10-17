package com.softserveinc.booklibrary.service.impl;

import com.softserveinc.booklibrary.dao.BookRepository;
import com.softserveinc.booklibrary.entity.Book;
import com.softserveinc.booklibrary.exception.NotValidIdValueException;
import com.softserveinc.booklibrary.exception.NullEntityException;
import com.softserveinc.booklibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl extends AbstractEntityService<Book, Integer> implements BookService {

	@Autowired
	public BookServiceImpl(BookRepository bookRepository) {
		repository = bookRepository;
	}

}
