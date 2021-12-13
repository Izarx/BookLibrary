package com.softserveinc.booklibrary.backend.pagination.filtering;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookFilter {

	private String name;
	private String authorName;
	private BigDecimal ratingFrom;
	private BigDecimal ratingTo;
	private Integer year;
	private String isbn;

}
