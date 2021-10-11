package com.softserveinc.booklibrary.dao;

public interface EntityRepository<T> {

	T create(T entity);

	T update(T entity);

	T getById(Integer id);

	boolean delete(T entity);

}
