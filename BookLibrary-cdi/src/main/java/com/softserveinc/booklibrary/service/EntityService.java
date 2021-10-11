package com.softserveinc.booklibrary.service;

public interface EntityService<T> {
	T create(T entity);

	T getById(Integer id);

	T update(T entity);

	void delete(Integer id);

}
