package com.softserveinc.booklibrary.repository.impl;

import com.softserveinc.booklibrary.repository.BookRepository;
import com.softserveinc.booklibrary.repository.ReviewRepository;
import com.softserveinc.booklibrary.entity.Book;
import com.softserveinc.booklibrary.entity.Review;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryImpl extends AbstractEntityRepository<Review> implements ReviewRepository {

	private final BookRepository bookRepository;

	public ReviewRepositoryImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public boolean isEntityValid(Review review) {
		String commenterName = review.getCommenterName();
		Integer rating = review.getRating();
		Book book = review.getBook();
		if (commenterName == null || commenterName.length() > Review.COMMENTER_NAME_LENGTH) {
			return false;
		}
		if (rating == null || rating < 1 || rating > 5) {
			return false;
		}
		return book != null && book.getBookId() != null;
	}
}
