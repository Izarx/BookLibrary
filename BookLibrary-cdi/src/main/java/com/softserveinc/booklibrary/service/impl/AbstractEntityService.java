package com.softserveinc.booklibrary.service.impl;

import javax.persistence.EntityNotFoundException;

import com.softserveinc.booklibrary.dao.EntityRepository;
import com.softserveinc.booklibrary.service.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

public abstract class AbstractEntityService<T> implements EntityService<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityService.class);
	protected EntityRepository<T> repository;

	@Override
	@Transactional
	public T create(T entity) {
		LOGGER.info("Service method starts new transaction: {}",
				TransactionAspectSupport.currentTransactionStatus().isNewTransaction());
		T persistedEntity = repository.create(entity);
		if (persistedEntity != null) {
			return persistedEntity;
		}
		throw new EntityNotFoundException();    // later must create custom exception
	}

	@Override
	public T getById(Integer id) {
		T entity = repository.getById(id);
		if (entity != null) {
			return entity;
		}
		throw new EntityNotFoundException();    // later must create custom exception
	}

	@Override
	@Transactional
	public T update(T entity) {
		return repository.update(entity);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		repository.delete(getById(id));
	}
}
