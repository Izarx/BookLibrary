package com.softserveinc.booklibrary.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.softserveinc.booklibrary.dao.EntityRepository;

public abstract class AbstractEntityRepository<T> implements EntityRepository<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	protected Class<T> type;

	@Override
	@Transactional
	public T create(T entity) {
		entityManager.persist(entity);
		return entity;
	}

	@Override
	@Transactional
	public T update(T entity) {
		entityManager.merge(entity);
		return entity;
	}

	@Override
	public T getById(Integer id) {
		return entityManager.find(type, id);
	}

	@Override
	@Transactional
	public boolean delete(Integer id) {
		T entity = getById(id);
		if (entity != null) {
			entityManager.remove(entity);
			return true;
		}
		return false;
	}
}
