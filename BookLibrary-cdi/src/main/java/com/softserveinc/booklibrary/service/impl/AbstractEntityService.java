package com.softserveinc.booklibrary.service.impl;

import com.softserveinc.booklibrary.dao.EntityRepository;
import com.softserveinc.booklibrary.service.EntityService;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractEntityService<T> implements EntityService<T> {

	protected EntityRepository<T> repository;

	@Override
	@Transactional
	public T create(T entity) {
		return repository.create(entity);
	}

	@Override
	public T getById(Integer id) {
		return repository.getById(id);
	}

	@Override
	@Transactional
	public T update(T entity) {
		return repository.update(entity);
	}

	@Override
	@Transactional
	public boolean delete(Integer id) {
		return repository.delete(id);
	}
}
