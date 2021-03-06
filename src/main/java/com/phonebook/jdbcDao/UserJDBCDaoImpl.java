package com.phonebook.jdbcDao;

import com.phonebook.dao.PersistenceException;
import com.phonebook.dao.UserDao;
import com.phonebook.model.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@Profile({"mysql"})
public class UserJDBCDaoImpl extends GeneralJDBCDao implements UserDao {
    private static final Logger LOGGER = LogManager.getLogger(UserJDBCDaoImpl.class);
    private static final String CREATE_QUERY = "INSERT INTO users (login, password, name) VALUES (?, ?, ?)";
    private static final String READ_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE users SET login = ?, password = ?, name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ";
    private static final String READ_ALL_QUERY = "SELECT * FROM users";
    private static final String READ_BY_LOGIN = "SELECT * FROM users WHERE login = ?";
    private static final String GENERATED_KEY_NAME = System.getProperty("GENERATED_KEY_NAME");

    @Autowired
    private RowMapper<User> userRowMapper;

    @Override
    public User create(User object) throws PersistenceException {
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, object.getLogin());
            statement.setString(2, object.getPassword());
            statement.setString(3, object.getName());
            return statement;
        }, holder);
        return read(parseLongFromHolder(holder));
    }

    @Override
    public User read(long id) throws PersistenceException {
        try {
            return getJdbcTemplate().queryForObject(READ_QUERY, new Object[]{id}, userRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(User object) throws PersistenceException {
        getJdbcTemplate().update(connection -> {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setString(1, object.getLogin());
            statement.setString(2, object.getPassword());
            statement.setString(3, object.getName());
            statement.setLong(4, object.getId());
            return statement;
        });
    }

    @Override
    public void delete(long id) throws PersistenceException {
        getJdbcTemplate().update(DELETE_QUERY + id);
    }

    @Override
    public List<User> readAll() throws PersistenceException {
        return getJdbcTemplate().query(READ_ALL_QUERY, userRowMapper);
    }

    @Override
    public User readBylogin(String login) throws PersistenceException {
        try {
            return getJdbcTemplate().queryForObject(READ_BY_LOGIN, new Object[]{login}, userRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
