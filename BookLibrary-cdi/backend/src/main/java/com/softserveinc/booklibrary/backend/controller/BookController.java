package com.softserveinc.booklibrary.backend.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.booklibrary.backend.dto.ApplicationMapper;
import com.softserveinc.booklibrary.backend.dto.AuthorDto;
import com.softserveinc.booklibrary.backend.dto.BookDto;
import com.softserveinc.booklibrary.backend.dto.BookNameDto;
import com.softserveinc.booklibrary.backend.entity.Book;
import com.softserveinc.booklibrary.backend.pagination.RequestOptions;
import com.softserveinc.booklibrary.backend.pagination.ResponseData;
import com.softserveinc.booklibrary.backend.pagination.filtering.BookFilter;
import com.softserveinc.booklibrary.backend.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

	private final ApplicationMapper appMapper;

	private final BookService bookService;

	public BookController(ApplicationMapper appMapper, BookService bookService) {
		this.appMapper = appMapper;
		this.bookService = bookService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookDto> getBook(@PathVariable Integer id) {
		LOGGER.info("Getting Book, BookController, the path variable is ID = {}", id);
		Book book = bookService.getById(id);
		if (book == null) {
			LOGGER.warn("Getting Book, BookController, Book with ID = {} not found", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} else {
			LOGGER.debug("Getting Book, BookController, Book with ID = {} is {}", id, book);
		}
		return ResponseEntity.ok(appMapper.bookToBookDto(book));
	}

	@GetMapping("/authors/{id}")
	public ResponseEntity<BookDto> getBookWithAuthors(@PathVariable Integer id) {
		LOGGER.info("Getting Book, BookController, the path variable is ID = {}", id);
		Book book = bookService.getByIdWithAuthors(id);
		if (book == null) {
			LOGGER.warn("Getting Book, BookController, Book with ID = {} not found", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} else {
			LOGGER.debug("Getting Book, BookController, Book with ID = {} is {}", id, book);
		}
		return ResponseEntity.ok(appMapper.bookToBookDto(book));
	}

	@PostMapping("/bulk-delete")
	public ResponseEntity<List<BookNameDto>> bulkDeleteBooks(
			@RequestBody List<Integer> booksIdsForDelete) {
		LOGGER.info("Bulk Delete Books, BookController, deleting books with IDs = {}", booksIdsForDelete);
		return ResponseEntity
				.ok(appMapper.listBooksToListBooksNameDto(
						bookService.bulkDeleteEntities(new ArrayList<>(booksIdsForDelete))));
	}

	@PostMapping
	public ResponseEntity<ResponseData<BookDto>> listBooks(@RequestBody RequestOptions<BookFilter> requestOptions) {
		LOGGER.debug("Getting Filtered Sorted Page of Books, BookController, Request options to fetch page of Books is {}", requestOptions);
		return ResponseEntity.status(HttpStatus.OK)
				.body(convertBookPageToBookDtoPage(
						bookService.listEntities(requestOptions)));
	}

	@PostMapping("/create")
	public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
		if (bookDto == null) {
			LOGGER.warn("Creating Book, BookController, Transfer object which received from UI is empty!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		LOGGER.debug("Creating Book, BookController, Transfer object which received from UI is {}", bookDto);
		return ResponseEntity.ok(appMapper
				.bookToBookDto(bookService
						.create(appMapper.bookDtoToBook(bookDto))));
	}

	@PutMapping("/update")
	public ResponseEntity<BookDto> updateBook(@RequestBody BookDto bookDto) {
		if (bookDto == null) {
			LOGGER.warn("Updating Book, BookController, Transfer object which received from UI with updating book information is empty!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		LOGGER.debug("Updating Book, BookController, Transfer object which received from UI with updating books information is {}", bookDto);
		return ResponseEntity.ok(appMapper
				.bookToBookDto(bookService
						.update(appMapper.bookDtoToBook(bookDto))));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<BookDto> deleteBook(@PathVariable Integer id) {
		LOGGER.info("Deleting Book, BookController, the path variable is ID = {}.", id);
		return bookService.delete(id) ?
				ResponseEntity.ok().build() :
				ResponseEntity.noContent().build(); // todo: reason?
	}

	private ResponseData<BookDto> convertBookPageToBookDtoPage(
			ResponseData<Book> responseData) {
		ResponseData<BookDto> bookDtoResponseData = new ResponseData<>();
		if (responseData != null) {
			LOGGER.debug("Converting Response Data with Books to Response Data with Books DTOs, BookController, " +
							"response data BEFORE converting: total number of books = {} ; list with books = {}",
					responseData.getTotalElements(), responseData.getContent());
			bookDtoResponseData.setTotalElements(responseData.getTotalElements());
			List<Book> content = responseData.getContent();
			bookDtoResponseData.setContent(appMapper.listBooksToListBooksDto(content));
		}
		else {
			LOGGER.warn("Converting Response Data with Books to Response Data with Books DTOs, BookController, " +
					"response data is empty!");
		}
		LOGGER.debug("Converting Response Data with Books to Response Data with Books DTOs, BookController, " +
						"response data AFTER converting: total number of books = {} ; size list of books = {}",
				bookDtoResponseData.getTotalElements(), bookDtoResponseData.getContent().size());
		return bookDtoResponseData;
	}

}
