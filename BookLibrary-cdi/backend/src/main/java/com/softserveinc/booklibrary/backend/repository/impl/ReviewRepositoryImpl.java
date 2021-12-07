package com.softserveinc.booklibrary.backend.repository.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.softserveinc.booklibrary.backend.dto.filtering.ReviewFilter;
import com.softserveinc.booklibrary.backend.entity.Book;
import com.softserveinc.booklibrary.backend.entity.Review;
import com.softserveinc.booklibrary.backend.pagination.RequestOptions;
import com.softserveinc.booklibrary.backend.repository.ReviewRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryImpl extends AbstractEntityRepository<Review, ReviewFilter> implements ReviewRepository {

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

	@Override
	protected void setOrdersByColumnsByDefault(List<Order> orderList, CriteriaBuilder builder, Root<Review> rootEntity) {}

	@Override
	protected List<Predicate> getFilteringParams(RequestOptions<ReviewFilter> options, CriteriaBuilder builder, Root<Review> rootEntity) {
		return Collections.emptyList();
	}

}
