package com.softserveinc.booklibrary.controller;

import com.softserveinc.booklibrary.entity.Author;
import com.softserveinc.booklibrary.entity.Book;
import com.softserveinc.booklibrary.service.AuthorService;
import com.softserveinc.booklibrary.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	private final AuthorService authorService;
	private final BookService bookService;

	public HomeController(AuthorService authorService, BookService bookService) {
		this.authorService = authorService;
		this.bookService = bookService;
	}

	@GetMapping(value = "/test")
	public String test() {
		Author author = authorService.getById(3);
		Book book = bookService.getById(2);
		LOGGER.info("Author {} with ID {}",
				author.getFirstName(), author.getAuthorId());
		LOGGER.info("*****************************************************");
		LOGGER.info("Book with ID {} is {} published at {} year",
				book.getBookId(), book.getName(), book.getPublisher());
		return "home";
	}
}
