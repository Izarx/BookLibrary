package com.softserveinc.booklibrary.backend.controller;

import java.util.HashMap;

import com.softserveinc.booklibrary.backend.dto.ApplicationMapper;
import com.softserveinc.booklibrary.backend.dto.AuthorDto;
import com.softserveinc.booklibrary.backend.dto.BookDto;
import com.softserveinc.booklibrary.backend.entity.Author;
import com.softserveinc.booklibrary.backend.entity.Book;
import com.softserveinc.booklibrary.backend.pagination.RequestOptions;
import com.softserveinc.booklibrary.backend.pagination.ResponseData;
import com.softserveinc.booklibrary.backend.service.BookService;
import org.apache.commons.lang3.ObjectUtils;
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
		Book book = bookService.getById(id);
		return book != null ? ResponseEntity.ok(appMapper.bookToBookDto(book))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping("/authors/{id}")
	public ResponseEntity<BookDto> getBookWithAuthors(@PathVariable Integer id) {
		Book book = bookService.getByIdWithAuthors(id);
		return book != null ? ResponseEntity.ok(appMapper.bookToBookDto(book))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping
	public ResponseEntity<ResponseData<BookDto>> listBooks(@RequestBody RequestOptions<BookDto> requestOptions) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(convertBookPageToBookDtoPage(
						bookService.listEntities(convertBookDtoRequestToBookRequest(requestOptions))));
	}

	@PostMapping("/create")
	public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
		if (bookDto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.ok(appMapper
				.bookToBookDto(bookService
						.create(appMapper.bookDtoToBook(bookDto))));
	}

	@PutMapping("/update")
	public ResponseEntity<BookDto> updateBook(@RequestBody BookDto bookDto) {
		if (bookDto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.ok(appMapper
				.bookToBookDto(bookService
						.update(appMapper.bookDtoToBook(bookDto))));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<BookDto> deleteBook(@PathVariable Integer id) {
		return bookService.delete(id) ?
				ResponseEntity.ok().build() :
				ResponseEntity.notFound().build();
	}

	private ResponseData<BookDto> convertBookPageToBookDtoPage(
			ResponseData<Book> responseData) {
		ResponseData<BookDto> entityDtoPage = new ResponseData<>();
		entityDtoPage.setTotalElements(responseData.getTotalElements());
		entityDtoPage.setContent(appMapper.listBooksToListBooksDto(responseData.getContent()));
		return entityDtoPage;
	}

	private RequestOptions<Book> convertBookDtoRequestToBookRequest(
			RequestOptions<BookDto> bookDtoRequestOptions
	) {
		RequestOptions<Book> options = new RequestOptions<>();
		options.setPageSize(bookDtoRequestOptions.getPageSize());
		options.setPageNumber(bookDtoRequestOptions.getPageNumber());
		options.setSorting(bookDtoRequestOptions.getSorting());
		BookDto filteredBook = bookDtoRequestOptions.getFilteredEntity();
		if (ObjectUtils.isNotEmpty(filteredBook)) {
			options.setFilteredEntity(appMapper.bookDtoToBook(filteredBook));
			options.setRanges(new HashMap<>());
			options.getRanges().put("bookRating", filteredBook.getBookRatingRange());
			options.getRanges().put("yearPublished", filteredBook.getYearPublishedRange());
		}
		return options;
	}


}
