package com.phonebook.service;

import com.phonebook.dao.PersistenceException;
import com.phonebook.model.Contact;
import com.phonebook.model.User;

import java.util.Map;

public interface ValidationService {
    Map<String, String> verifyUser(User user, String passwordConfirmation) throws PersistenceException;

    Map<String, String> verifyContact(Contact contact);
}
