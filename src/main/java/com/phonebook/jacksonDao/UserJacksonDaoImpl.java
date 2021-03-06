package com.phonebook.jacksonDao;

import com.phonebook.dao.PersistenceException;
import com.phonebook.dao.UserDao;
import com.phonebook.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@Profile("default")
public class UserJacksonDaoImpl extends JacksonDaoSupport implements UserDao {

    @Override
    public User readBylogin(String login) throws PersistenceException {
        JsonPhonebookModel data = readData();
        if (data.getUsers() != null) {
            return data.getUsers().get(login);
        } else {
            return null;
        }
    }

    @Override
    public User create(User object) throws PersistenceException {
        JsonPhonebookModel data = readData();
        long userId = data.getUserCount() + 1;
        object.setId(userId);
        object.setContacts(new ArrayList<>());
        data.setUserCount(userId);
        if (data.getUsers() == null) {
            data.setUsers(new HashMap<>());
        }
        data.getUsers().put(object.getLogin(), object);
        writeData(data);
        return object;
    }

    @Override
    public User read(long id) throws PersistenceException {
        JsonPhonebookModel data = readData();
        return data.getUsers().entrySet().stream().filter(entry -> entry.getValue().getId() == id).findFirst().map(Map.Entry::getValue).orElse(null);
    }

    @Override
    public void update(User object) throws PersistenceException {
        JsonPhonebookModel data = readData();
        data.getUsers().values().stream().filter(user -> user.getId() == object.getId()).findFirst().ifPresent(user -> data.getUsers().remove(user.getLogin()));
        data.getUsers().put(object.getLogin(), object);
        writeData(data);
    }

    @Override
    public void delete(long id) throws PersistenceException {
        JsonPhonebookModel data = readData();
        User user = read(id);
        data.getUsers().remove(user.getLogin());
        writeData(data);
    }

    @Override
    public List<User> readAll() throws PersistenceException {
        return new ArrayList<>(readData().getUsers().values());
    }
}
