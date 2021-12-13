package com.softserveinc.booklibrary.backend.repository.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.softserveinc.booklibrary.backend.entity.AbstractEntity;
import com.softserveinc.booklibrary.backend.exception.NotValidEntityException;
import com.softserveinc.booklibrary.backend.exception.NotValidIdException;
import com.softserveinc.booklibrary.backend.pagination.RequestOptions;
import com.softserveinc.booklibrary.backend.pagination.ResponseData;
import com.softserveinc.booklibrary.backend.pagination.SortableColumn;
import com.softserveinc.booklibrary.backend.pagination.SortingDirection;
import com.softserveinc.booklibrary.backend.repository.EntityRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractEntityRepository<T extends AbstractEntity<? extends Serializable>, V> implements EntityRepository<T, V> {
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
		LOGGER.info("Create {}, {}, Creating object ...............", type.getSimpleName(), getClass().getSimpleName());
		LOGGER.debug("Create {}, {}, BEFORE persist object = {}", type.getSimpleName(), getClass().getSimpleName(), entity);
		if (!isEntityValid(entity)) {
			throw new NotValidEntityException(entity);
		}
		Serializable id = entity.getEntityId();
		LOGGER.debug("Create {}, {}, The ID of creating object = {}", type.getSimpleName(), getClass().getSimpleName(), id);
		if (id != null) {
			throw new NotValidIdException(id);
		}
		entityManager.persist(entity);
		LOGGER.info("Create {}, {}, Object of entity were persisted and ID = {}", type.getSimpleName(), getClass().getSimpleName(), entity.getEntityId());
		return entity;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public T update(T entity) {
		LOGGER.info("Update {}, {}, Updating object ...............", type.getSimpleName(), getClass().getSimpleName());
		LOGGER.debug("Update {}, {}, BEFORE merge object = {}", type.getSimpleName(), getClass().getSimpleName(), entity);
		if (!isEntityValid(entity)) {
			throw new NotValidEntityException(entity);
		}
		Serializable id = entity.getEntityId();
		LOGGER.debug("Update {}, {}, The ID of updating object = {}", type.getSimpleName(), getClass().getSimpleName(), id);
		if (id == null || entityManager.find(type, id) == null) {
			throw new NotValidIdException(id);
		}
		T mergedEntity = entityManager.merge(entity);
		LOGGER.info("Update {}, {}, Object of entity were merged and = {}", type.getSimpleName(), getClass().getSimpleName(), mergedEntity);
		return mergedEntity;
	}

	@Override
	public T getById(Serializable id) {
		LOGGER.info("Getting {}, {}, The ID of getting object = {}", type.getSimpleName(), getClass().getSimpleName(), id);
		if (id == null) {
			throw new NotValidIdException(null);
		}
		T entity = entityManager.find(type, id);
		LOGGER.info("Getting {}, {}, Object of getting entity = {}", type.getSimpleName(), getClass().getSimpleName(), entity);
		return entity;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public boolean delete(Serializable id) {
		LOGGER.info("Deleting {}, {}, The ID of deleting object = {}", type.getSimpleName(), getClass().getSimpleName(), id);
		if (id == null) {
			LOGGER.warn("Deleting {}, {}, The ID of deleting object is null", type.getSimpleName(), getClass().getSimpleName());
			return false;
		}
		T entity = entityManager.find(type, id);
		if (entity != null) {
			entityManager.remove(entity);
			LOGGER.info("Deleting {}, {}, Entity object with ID = {} was deleted", type.getSimpleName(), getClass().getSimpleName(), id);
			return true;
		}
		LOGGER.info("Deleting {}, {}, Entity object with ID = {} wasn't found", type.getSimpleName(), getClass().getSimpleName(), id);
		return true;
	}

	@Override
	public abstract boolean isEntityValid(T entity); // todo: remove this method from here

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<T> getAll() {
		CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(type);
		criteriaQuery.select(criteriaQuery.from(type));
		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseData<T> listEntities(RequestOptions<V> requestOptions) {
		LOGGER.info("Getting Filtered Sorted Page of {}, {}", type.getSimpleName(), getClass().getSimpleName());
		ResponseData<T> responseData = new ResponseData<>();
		int pageSize = requestOptions.getPageSize();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> criteriaQuery = builder.createQuery(type);
		Root<T> rootEntity = criteriaQuery.from(type);
		criteriaQuery.where(getFilteringParams(requestOptions, builder, rootEntity).toArray(new Predicate[]{}));
		responseData.setTotalElements(getCountOfEntities(requestOptions, builder));
		LOGGER.info("Getting Filtered Sorted Page of {}, {}, {} objects were found in DB according to request options", type.getSimpleName(), getClass().getSimpleName(), responseData.getTotalElements());
		criteriaQuery.orderBy(setOrdersByColumns(requestOptions.getSorting(), builder, rootEntity));
		List<T> getAll = entityManager
				.createQuery(criteriaQuery)
				.setFirstResult(requestOptions.getPageNumber() * pageSize)
				.setMaxResults(pageSize)
				.getResultList();
		responseData.setContent(getAll);
		LOGGER.debug("Getting Filtered Sorted Page of {}, {}, Content to response data is {}", type.getSimpleName(), getClass().getSimpleName(), responseData.getContent());
		return responseData;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public List<T> bulkDeleteEntities(List<Serializable> entitiesIdsForDelete) {
		LOGGER.info("Bulk Delete of {}, {}", type.getSimpleName(), getClass().getSimpleName());
		if (CollectionUtils.isNotEmpty(entitiesIdsForDelete)) {
			LOGGER.info("Bulk Delete of {}, {}, {} object(s) for deleting", type.getSimpleName(), getClass().getSimpleName(), entitiesIdsForDelete.size());
		}
		String columnName = Stream.of(type.getDeclaredFields())
				.filter(f -> f.isAnnotationPresent(Id.class))
				.findFirst()
				.orElseThrow()
				.getName();
		LOGGER.info("Bulk Delete of {}, {}, Name of column which will be used in criteria is \"{}\"", type.getSimpleName(), getClass().getSimpleName(), columnName);
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> criteriaQuery = builder.createQuery(type);
		CriteriaDelete<T> criteriaDelete = builder.createCriteriaDelete(type);
		Root<T> rootDelete = criteriaDelete.from(type);

		List<T> unavailableToDeleteEntities =
				getUnavailableToDeleteEntities(entitiesIdsForDelete, criteriaQuery);
		if (CollectionUtils.isNotEmpty(unavailableToDeleteEntities)) {
			LOGGER.warn("Bulk Delete of {}, {}, Can't delete {} object(s)", type.getSimpleName(), getClass().getSimpleName(), unavailableToDeleteEntities.size());
			LOGGER.debug("Bulk Delete of {}, {}, Can't delete {}", type.getSimpleName(), getClass().getSimpleName(), unavailableToDeleteEntities);
			unavailableToDeleteEntities
					.forEach(entity -> entitiesIdsForDelete.remove(entity.getEntityId()));
		}
		criteriaDelete.where(rootDelete.get(columnName).in(entitiesIdsForDelete));
		entityManager.createQuery(criteriaDelete).executeUpdate();
		LOGGER.info("Bulk Delete of {}, {}, IDs object(s) which was deleted {}", type.getSimpleName(), getClass().getSimpleName(), unavailableToDeleteEntities.size());
		return unavailableToDeleteEntities;
	}

	protected List<T> getUnavailableToDeleteEntities(List<Serializable> entitiesIdsForDelete,
	                                                 CriteriaQuery<T> criteriaQuery) {
		return Collections.emptyList();
	}

	protected void setOrdersByColumnsByDefault(List<Order> orderList, CriteriaBuilder builder,
	                                           Root<T> rootEntity) {
	}

	/**
	 * Create List of Orders from sortable columns to order db entities by it
	 *
	 * @param sortableColumns columns, which we receive from UI, they consist from name field and direction for sorting
	 * @param builder         criteria builder
	 * @param rootEntity      root
	 * @return list of sorting orders for each sortable column which was activated
	 */

	private List<Order> setOrdersByColumns(List<SortableColumn> sortableColumns, CriteriaBuilder builder,
	                                       Root<T> rootEntity) {
		List<Order> orderList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(sortableColumns)) {
			for (SortableColumn column : sortableColumns) {
				if (SortingDirection.ASC == column.getDirection()) {
					orderList.add(builder.asc(rootEntity.get(column.getName())));
				} else if (SortingDirection.DESC == column.getDirection()) {
					orderList.add(builder.desc(rootEntity.get(column.getName())));
				}
			}
		}
		// Default sorting for manage entities pages could be by creation date
		else {
			setOrdersByColumnsByDefault(orderList, builder, rootEntity);
		}
		return orderList;
	}

	/**
	 * Create list of predicates to obtain objects according to filtering options
	 *
	 * @param options    request options which consists entity with filtering fields
	 * @param builder    criteria builder
	 * @param rootEntity root
	 * @return list of predicates according to request options
	 */
	protected List<Predicate> getFilteringParams(RequestOptions<V> options,
	                                             CriteriaBuilder builder,
	                                             Root<T> rootEntity) {
		return Collections.emptyList();
	}

	public Integer getCountOfEntities(RequestOptions<V> options,
	                                  CriteriaBuilder builder) {
		CriteriaQuery<Long> countCriteriaQuery = builder.createQuery(Long.class);   // todo: not use Long here
		Root<T> rootEntity = countCriteriaQuery.from(type);
		countCriteriaQuery.select(builder.count(rootEntity));
		countCriteriaQuery.where(getFilteringParams(options, builder, rootEntity).toArray(new Predicate[]{}));
		return entityManager.createQuery(countCriteriaQuery).getSingleResult().intValue();
	}


}
