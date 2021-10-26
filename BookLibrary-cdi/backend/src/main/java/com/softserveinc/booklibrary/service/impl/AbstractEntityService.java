package com.softserveinc.booklibrary.service.impl;

import java.io.Serializable;

import com.softserveinc.booklibrary.repository.EntityRepository;
import com.softserveinc.booklibrary.entity.MyAppEntity;
import com.softserveinc.booklibrary.exception.NotValidEntityException;
import com.softserveinc.booklibrary.exception.NotValidIdException;
import com.softserveinc.booklibrary.service.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractEntityService<T extends MyAppEntity<? extends Serializable>> implements EntityService<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityService.class);
	protected EntityRepository<T> repository;

	@Override
	@Transactional
	public T create(T entity) {
		if (!repository.isEntityValid(entity)) {
			throw new NotValidEntityException();
		}
		Serializable id = entity.getEntityId();
		if (id != null) {
			throw new NotValidIdException(id);
		}
		return repository.create(entity);
	}

	@Override
	@Transactional
	public T update(T entity) {
		if (!repository.isEntityValid(entity)) {
			throw new NotValidEntityException();
		}
		Serializable id = entity.getEntityId();
		if (id == null || repository.getById(id) == null) {
			throw new NotValidIdException(id);
		}
		return repository.update(entity);
	}

	@Override
	public T getById(Serializable id) {
		if (id == null) {
			throw new NotValidIdException(null);
		}
		return repository.getById(id);
	}

	@Override
	@Transactional
	public boolean delete(Serializable id) {
		if (id == null) {
			return false;
		}
		return repository.delete(id);
	}
}
