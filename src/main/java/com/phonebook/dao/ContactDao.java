package com.phonebook.dao;

import com.phonebook.model.Contact;
import com.phonebook.model.User;

import java.util.List;


public interface ContactDao extends GenericDao<Contact> {
    List<Contact> getAllContactsByUser(User user) throws PersistenceException;
}
