package com.softserveinc.booklibrary.service.impl;

import com.softserveinc.booklibrary.dao.EntityRepository;
import com.softserveinc.booklibrary.entity.EntityLibrary;
import com.softserveinc.booklibrary.exception.NotValidEntityException;
import com.softserveinc.booklibrary.exception.NotValidIdException;
import com.softserveinc.booklibrary.service.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractEntityService<T extends EntityLibrary<K>, K> implements EntityService<T, K> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityService.class);
	protected EntityRepository<T, K> repository;

	@Override
	@Transactional
	public T create(T entity) {
		if (!repository.isEntityValid(entity)) {
			throw new NotValidEntityException();
		}
		K id = entity.getEntityId();
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
		K id = entity.getEntityId();
		if (id == null || repository.getById(id) == null) {
			throw new NotValidIdException(id);
		}
		return repository.update(entity);
	}

	@Override
	public T getById(K id) {
		if (id == null) {
			throw new NotValidIdException(null);
		}
		return repository.getById(id);
	}

	@Override
	@Transactional
	public boolean delete(K id) {
		if (id == null) {
			return false;
		}
		return repository.delete(id);
	}
}
