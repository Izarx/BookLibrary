package com.softserveinc.booklibrary.dao.impl;

import com.softserveinc.booklibrary.dao.ReviewRepository;
import com.softserveinc.booklibrary.entity.Review;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryAbstract extends AbstractEntityRepository<Review> implements ReviewRepository {

	{
		type = Review.class;
	}

}
