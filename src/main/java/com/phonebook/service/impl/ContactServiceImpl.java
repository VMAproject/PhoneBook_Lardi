package com.phonebook.service.impl;

import com.phonebook.dao.ContactDao;
import com.phonebook.dao.PersistenceException;
import com.phonebook.model.Contact;
import com.phonebook.model.User;
import com.phonebook.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactDao contactDao;

    @Override
    public Contact save(Contact object) throws PersistenceException {
        if (object.getId() == 0) return contactDao.create(object);
        else {
            contactDao.update(object);
            return object;
        }
    }

    @Override
    public void delete(long id) throws PersistenceException {
        contactDao.delete(id);
    }

    @Override
    public List<Contact> findAllUsersContacts(User user) throws PersistenceException {
        return contactDao.getAllContactsByUser(user);
    }

    @Override
    public Contact findContactById(long id) throws PersistenceException {
        return contactDao.read(id);
    }
}
