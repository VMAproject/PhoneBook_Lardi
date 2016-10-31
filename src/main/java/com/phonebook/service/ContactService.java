package com.phonebook.service;

import com.phonebook.dao.PersistenceException;
import com.phonebook.model.Contact;
import com.phonebook.model.User;

import java.util.List;


public interface ContactService extends GenericService<Contact> {
    List<Contact> findAllUsersContacts(User user) throws PersistenceException;

    Contact findContactById(long id) throws PersistenceException;
}
