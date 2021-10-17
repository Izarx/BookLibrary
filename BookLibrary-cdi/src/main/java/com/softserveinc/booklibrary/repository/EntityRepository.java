package com.softserveinc.booklibrary.repository;

import java.io.Serializable;

import com.softserveinc.booklibrary.entity.MyAppEntity;

public interface EntityRepository<T extends MyAppEntity<? extends Serializable>> {

	T create(T entity);

	T update(T entity);

	T getById(Serializable id);

	boolean delete(Serializable id);

	boolean isEntityValid(T entity);

}
