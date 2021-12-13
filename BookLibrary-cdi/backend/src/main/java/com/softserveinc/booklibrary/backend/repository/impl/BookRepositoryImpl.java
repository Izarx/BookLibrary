package com.softserveinc.booklibrary.backend.repository.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.softserveinc.booklibrary.backend.entity.Author;
import com.softserveinc.booklibrary.backend.entity.Book;
import com.softserveinc.booklibrary.backend.pagination.RequestOptions;
import com.softserveinc.booklibrary.backend.pagination.filtering.BookFilter;
import com.softserveinc.booklibrary.backend.repository.BookRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.BetweenExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl extends AbstractEntityRepository<Book, BookFilter> implements BookRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(BookRepositoryImpl.class);

	@Override
	public boolean isEntityValid(Book book) {
		LOGGER.info("BookRepositoryImpl, Validating book .......................");
		if (book == null) {
			LOGGER.warn("BookRepositoryImpl, Book object is null!");
			return false;
		}
		String name = book.getName();
		Integer yearPublished = book.getYearPublished();
		String publisher = book.getPublisher();
		if (!book.getIsbn().matches("[0-9]+")) {
			LOGGER.warn("BookRepositoryImpl, Book \"{}\" is not valid by ISBN parameter {}", name, book.getIsbn());
			return false;
		}
		if (name == null || name.length() > Book.NAME_LENGTH) {
			LOGGER.warn("BookRepositoryImpl, Book is not valid by NAME parameter");
			return false;
		}
		if (yearPublished == null || yearPublished < 0 || yearPublished > LocalDate.now().getYear()) {
			LOGGER.warn("BookRepositoryImpl, Book \"{}\" is not valid by YEAR PUBLISHED parameter {}", name, book.getYearPublished());
			return false;
		}
		return publisher != null && publisher.length() <= Book.PUBLISHER_LENGTH;
	}

	@Override
	protected void setOrdersByColumnsByDefault(List<Order> orderList,
	                                           CriteriaBuilder builder,
	                                           Root<Book> rootEntity) {
		orderList.add(builder.desc(rootEntity.get("bookRating")));
		orderList.add(builder.desc(rootEntity.get("createDate")));
	}


	@Override
	protected List<Predicate> getFilteringParams(RequestOptions<BookFilter> options,
	                                             CriteriaBuilder builder,
	                                             Root<Book> rootEntity) {
		List<Predicate> predicates = new ArrayList<>();
		if (options == null) {
			return predicates;
		}
		BookFilter bookFilter = options.getFilteredEntity();
		String name = null;
		String authorName = null;
		BigDecimal bookRatingFrom = null;
		BigDecimal bookRatingTo = null;
		Integer year = null;
		String isbn = null;
		if (bookFilter != null) {
			name = bookFilter.getName();
			authorName = bookFilter.getAuthorName();
			bookRatingFrom = bookFilter.getRatingFrom();
			bookRatingTo = bookFilter.getRatingTo();
			year = bookFilter.getYear();
			isbn = bookFilter.getIsbn();
		}
		if (StringUtils.isNotEmpty(name)) {
			predicates.add(builder.like(rootEntity.get("name"), '%' + name + '%'));
		}
		if (StringUtils.isNotEmpty(authorName)) {
			Join<Book, Author> bookAuthorJoin = rootEntity.join("authors");
			predicates.add(bookAuthorJoin.on(builder.or(builder.like(bookAuthorJoin.get("firstName"),
							'%' + authorName + '%'),
					builder.like(bookAuthorJoin.get("lastName"),
							'%' + authorName + '%'))).getOn());
		}
		if (bookRatingFrom != null) {
			predicates.add(builder.greaterThanOrEqualTo(rootEntity.get("bookRating"), bookRatingFrom));
		}
		if (bookRatingTo != null) {
			predicates.add(builder.lessThanOrEqualTo(rootEntity.get("bookRating"), bookRatingTo));
		}
		if (year != null) {
			predicates.add(builder.equal(rootEntity.get("yearPublished"), year));
		}
		if (isbn != null) {
			predicates.add(builder.like(rootEntity.get("isbn"), '%' + isbn + '%'));
		}
		return predicates;
	}
}
