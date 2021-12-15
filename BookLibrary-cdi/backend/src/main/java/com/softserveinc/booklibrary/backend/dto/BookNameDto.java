package com.softserveinc.booklibrary.backend.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookNameDto {

	private Integer bookId;
	private String name;
	private BigDecimal bookRating;

}
