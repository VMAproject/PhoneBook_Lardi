package com.phonebook.dao;

import com.phonebook.model.User;


public interface UserDao extends GenericDao<User> {
    User readBylogin(String login) throws PersistenceException;
}
