package com.softserveinc.booklibrary.service;

import com.softserveinc.booklibrary.entity.Author;

public interface AuthorService {

    public Author create(Author author);
    public Author getById(Integer id);
    public Author update(Author author);
    public void delete(Integer id);
}
