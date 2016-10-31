package com.phonebook.service;

import com.phonebook.dao.PersistenceException;


public interface GenericService<T> {
    T save(T object) throws PersistenceException;

    void delete(long id) throws PersistenceException;
}
