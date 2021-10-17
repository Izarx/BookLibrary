package com.softserveinc.booklibrary.service.impl;

import com.softserveinc.booklibrary.dao.ReviewRepository;
import com.softserveinc.booklibrary.entity.Review;
import com.softserveinc.booklibrary.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl extends AbstractEntityService<Review, Integer> implements ReviewService {

	@Autowired
	public ReviewServiceImpl(ReviewRepository reviewRepository) {
		repository = reviewRepository;
	}

}
