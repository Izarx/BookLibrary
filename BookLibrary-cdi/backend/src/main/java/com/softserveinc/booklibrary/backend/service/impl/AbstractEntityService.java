package com.softserveinc.booklibrary.backend.service.impl;

import java.io.Serializable;
import java.util.List;

import com.softserveinc.booklibrary.backend.dto.MyAppDto;
import com.softserveinc.booklibrary.backend.dto.paging.MyPage;
import com.softserveinc.booklibrary.backend.dto.paging.PageConstructor;
import com.softserveinc.booklibrary.backend.entity.AbstractEntity;
import com.softserveinc.booklibrary.backend.exception.NotValidEntityException;
import com.softserveinc.booklibrary.backend.exception.NotValidIdException;
import com.softserveinc.booklibrary.backend.repository.EntityRepository;
import com.softserveinc.booklibrary.backend.service.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractEntityService<T extends AbstractEntity<? extends Serializable>> implements EntityService<T> {

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

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<T> getAll() {
		return repository.getAll();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public MyPage<T> listEntities(PageConstructor pageConstructor) {
		return repository.listEntities(pageConstructor);
	}

	@Override
	public MyPage<? extends MyAppDto<T>> convertPageEntityDto(MyPage<T> page) {
		MyPage<MyAppDto<T>> entityDtoPage = new MyPage<>();
		entityDtoPage.setPageConstructor(page.getPageConstructor());
		entityDtoPage.setLast(page.getLast());
		entityDtoPage.setTotalPages(page.getTotalPages());
		entityDtoPage.setTotalElements(page.getTotalElements());
		entityDtoPage.setFirst(page.getFirst());
		entityDtoPage.setNumberOfFirstPageElement(page.getNumberOfFirstPageElement());
		entityDtoPage.setNumberOfElements(page.getNumberOfElements());
		return entityDtoPage;
	}
}
