package com.softserveinc.booklibrary.backend.service.impl;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.softserveinc.booklibrary.backend.dto.AuthorDto;
import com.softserveinc.booklibrary.backend.entity.Author;
import com.softserveinc.booklibrary.backend.entity.Book;
import com.softserveinc.booklibrary.backend.pagination.RequestOptions;
import com.softserveinc.booklibrary.backend.pagination.ResponseData;
import com.softserveinc.booklibrary.backend.pagination.filtering.BookFilter;
import com.softserveinc.booklibrary.backend.repository.BookRepository;
import com.softserveinc.booklibrary.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookServiceImpl extends AbstractEntityService<Book, BookFilter> implements BookService {

	@Autowired
	public BookServiceImpl(BookRepository bookRepository) {
		repository = bookRepository;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseData<Book> listEntities(RequestOptions<BookFilter> requestOptions) {
		ResponseData<Book> bookResponseData = super.listEntities(requestOptions);
		bookResponseData.getContent().forEach(b -> b.setAuthors(b.getAuthors().stream().collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Author::getFirstName))))));
		return bookResponseData;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Book getByIdWithAuthors(Integer id) {
		Book book = repository.getById(id);
		if (book != null) {
			book.getAuthors().size();
		}
		return book;
	}
}
