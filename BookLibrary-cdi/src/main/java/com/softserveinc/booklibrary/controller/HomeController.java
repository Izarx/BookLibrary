package com.softserveinc.booklibrary.controller;

import com.softserveinc.booklibrary.entity.Author;
import com.softserveinc.booklibrary.entity.Book;
import com.softserveinc.booklibrary.service.AuthorService;
import com.softserveinc.booklibrary.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping(value = "/create")
	public String create() {
		Author author = new Author();
		author.setFirstName("Ihor");
		author.setLastName("Zakharko");
		Book book = new Book();
		book.setName("Book name test");
		book.setYearPublished(2021);
		book.setIsbn(90123456781234L);
		book.setPublisher("Test publisher");

		authorService.create(author);
		bookService.create(book);

		LOGGER.info("Author {} with ID {}",
				author.getFirstName(), author.getAuthorId());
		LOGGER.info("*****************************************************");
		LOGGER.info("Book with ID {} is {} published at {} year",
				book.getBookId(), book.getName(), book.getYearPublished());
		return "CREATE";
	}

	@GetMapping(value = "/delete")
	public String delete(@RequestParam int authorId,
	                     @RequestParam int bookId) {
		Author author = authorService.getById(authorId);
		Book book = bookService.getById(bookId);
		LOGGER.info("Author {} with ID {}",
				author.getFirstName(), author.getAuthorId());
		LOGGER.info("*****************************************************");
		LOGGER.info("Book with ID {} is {} published at {} year",
				book.getBookId(), book.getName(), book.getYearPublished());
		authorService.delete(authorId);
		bookService.delete(bookId);
		return "DELETE";
	}
}
