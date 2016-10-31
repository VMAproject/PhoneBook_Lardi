package com.phonebook.service;

import com.phonebook.dao.PersistenceException;
import com.phonebook.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends GenericService<User>, UserDetailsService {
    User getUserByLogin(String login) throws PersistenceException;
}
