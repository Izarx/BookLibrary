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

		Author actualAuthor = authorService.create(author);

		LOGGER.info("Author {} with ID {}", actualAuthor, actualAuthor.getAuthorId());
		LOGGER.info("*****************************************************");

		actualAuthor.setFirstName("NotIhor");
		Author updatedAuthor = authorService.update(actualAuthor);
		LOGGER.info("Updatetd Author {} with ID {}", updatedAuthor, updatedAuthor.getAuthorId());
		LOGGER.info("*****************************************************");
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
