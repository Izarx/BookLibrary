package com.softserveinc.booklibrary.backend.repository.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import com.softserveinc.booklibrary.backend.dto.paging.ApplicationResponsePage;
import com.softserveinc.booklibrary.backend.dto.paging.ApplicationRequestPage;
import com.softserveinc.booklibrary.backend.dto.sorting.SortableColumn;
import com.softserveinc.booklibrary.backend.entity.AbstractEntity;
import com.softserveinc.booklibrary.backend.exception.NotValidEntityException;
import com.softserveinc.booklibrary.backend.exception.NotValidIdException;
import com.softserveinc.booklibrary.backend.repository.EntityRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractEntityRepository<T extends AbstractEntity<? extends Serializable>> implements EntityRepository<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityRepository.class);
	private final Class<T> type;
	@PersistenceContext
	protected EntityManager entityManager;

	protected AbstractEntityRepository() {
		type = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public T create(T entity) {
		if (!isEntityValid(entity)) {
			throw new NotValidEntityException();
		}
		Serializable id = entity.getEntityId();
		if (id != null) {
			throw new NotValidIdException(id);
		}
		entityManager.persist(entity);
		return entity;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public T update(T entity) {
		if (!isEntityValid(entity)) {
			throw new NotValidEntityException();
		}
		Serializable id = entity.getEntityId();
		if (id == null || entityManager.find(type, id) == null) {
			throw new NotValidIdException(id);
		}
		return entityManager.merge(entity);
	}

	@Override
	public T getById(Serializable id) {
		if (id == null) {
			throw new NotValidIdException(null);
		}
		return entityManager.find(type, id);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public boolean delete(Serializable id) {
		if (id == null) {
			return false;
		}
		T entity = entityManager.find(type, id);
		if (entity != null) {
			entityManager.remove(entity);
			return true;
		}
		return false;
	}

	@Override
	public abstract boolean isEntityValid(T entity);

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<T> getAll() {
		CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(type);
		CriteriaQuery<T> getAll = criteriaQuery.select(criteriaQuery.from(type));
		return entityManager.createQuery(getAll).getResultList();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public ApplicationResponsePage<T> listEntities(ApplicationRequestPage applicationRequestPage) {
		ApplicationResponsePage<T> responsePage = new ApplicationResponsePage<>();
		int pageSize = applicationRequestPage.getPageSize();
		int pageNumber = applicationRequestPage.getPageNumber();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countCriteriaQuery = builder.createQuery(Long.class);
		countCriteriaQuery.select(builder.count(countCriteriaQuery.from(type)));
		Long totalElements = entityManager.createQuery(countCriteriaQuery).getSingleResult();

		responsePage.setTotalElements(totalElements.intValue());

		CriteriaQuery<T> criteriaQuery = builder.createQuery(type);
		Root<T> rootEntity = criteriaQuery.from(type);
		CriteriaQuery<T> selectEntities = criteriaQuery.select(rootEntity);

		criteriaQuery.orderBy(setOrdersByColumns(applicationRequestPage.getSorting(), builder, rootEntity));
		List<T> getAll = entityManager
				.createQuery(selectEntities)
				.setFirstResult(pageNumber * pageSize)
				.setMaxResults(pageSize)
				.getResultList();
		responsePage.setContent(getAll);
		return responsePage;
	}

	/**
	 * Create List of Orders from sortable columns to order db entities by it
	 * @param sortableColumns columns, which we receive from UI, they consist from name field and direction for sorting
	 * @param builder
	 * @param rootEntity
	 * @return
	 */

	protected List<Order> setOrdersByColumns (List<SortableColumn> sortableColumns,
	                                          CriteriaBuilder builder,
	                                          Root<T> rootEntity) {
		List<Order> orderList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(sortableColumns)) {
			for (SortableColumn column : sortableColumns) {
				if ("asc".equals(column.getDirection())){
					orderList.add(builder.asc(rootEntity.get(column.getName())));
				}
				if ("desc".equals(column.getDirection())){
					orderList.add(builder.desc(rootEntity.get(column.getName())));
				}
			}
		}
		// Default sorting for manage entities pages could be by creation date
		if (CollectionUtils.isEmpty(sortableColumns)) {
			orderList.add(builder.desc(rootEntity.get("createDate")));
		}
		return orderList;
	}
}
