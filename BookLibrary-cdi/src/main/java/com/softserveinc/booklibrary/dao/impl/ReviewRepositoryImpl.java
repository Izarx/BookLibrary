package com.softserveinc.booklibrary.dao.impl;

import com.softserveinc.booklibrary.dao.BookRepository;
import com.softserveinc.booklibrary.dao.ReviewRepository;
import com.softserveinc.booklibrary.entity.Book;
import com.softserveinc.booklibrary.entity.Review;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryImpl extends AbstractEntityRepository<Review, Integer> implements ReviewRepository {

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
		if (book == null || book.getBookId() == null ||
				bookRepository.getById(book.getBookId()) == null || !bookRepository.isEntityValid(book)) {
			return false;
		}
		return true;
	}
}
