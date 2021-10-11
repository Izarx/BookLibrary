package com.softserveinc.booklibrary.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.softserveinc.booklibrary.dao.EntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

public abstract class AbstractEntityRepository<T> implements EntityRepository<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityRepository.class);

	@PersistenceContext
	protected EntityManager entityManager;

	protected Class<T> type;

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public T create(T entity) {
		LOGGER.info("Repository method starts new transaction: {}",
				TransactionAspectSupport.currentTransactionStatus().isNewTransaction());
		if (entity != null) {
			entityManager.persist(entity);
		}
		return entity;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public T update(T entity) {
		if (entity != null) {
			entityManager.merge(entity);
		}
		return entity;
	}

	@Override
	public T getById(Integer id) {
		return entityManager.find(type, id);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public boolean delete(T entity) {
		if (entity != null) {
			entityManager.remove(entity);
			return true;
		}
		return false;
	}
}
