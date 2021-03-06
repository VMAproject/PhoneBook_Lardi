package com.phonebook.jdbcDao;

import com.phonebook.dao.ContactDao;
import com.phonebook.dao.PersistenceException;
import com.phonebook.model.Contact;
import com.phonebook.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


@Repository
@Profile({"mysql"})
public class ContactJDBCDaoImpl extends GeneralJDBCDao implements ContactDao {

    private static final String CREATE_QUERY = "INSERT INTO contact (lastname, firstname, middlename, mobilephone, " +
            "homephone, address, email, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String READ_QUERY = "SELECT * FROM contact WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE contact SET lastname = ?, firstname = ?, middlename = ?, " +
            "mobilephone = ?, homephone = ?, address = ?, email = ?, user_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM contact WHERE id = ";
    private static final String READ_ALL_QUERY = "SELECT * FROM contact";
    private static final String READ_ALL_BY_USER = "SELECT * FROM contact WHERE user_id = ?";

    @Autowired
    private RowMapper<Contact> contactRowMapper;

    @Override
    public Contact create(Contact object) throws PersistenceException {
        KeyHolder holder = new GeneratedKeyHolder();
        getJdbcTemplate().update(connection -> {
            return makeBasePrepareStatment(CREATE_QUERY, object, connection);
        }, holder);

        return read(parseLongFromHolder(holder));
    }

    @Override
    public Contact read(long id) throws PersistenceException {
        try {
            return getJdbcTemplate().queryForObject(READ_QUERY, new Object[]{id}, contactRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(Contact object) throws PersistenceException {
        getJdbcTemplate().update(connection -> {
            PreparedStatement statement = makeBasePrepareStatment(UPDATE_QUERY, object, connection);
            statement.setLong(9, object.getId());
            return statement;
        });
    }

    @Override
    public void delete(long id) throws PersistenceException {
        getJdbcTemplate().update(DELETE_QUERY + id);
    }

    @Override
    public List<Contact> readAll() throws PersistenceException {
        return getJdbcTemplate().query(READ_ALL_QUERY, contactRowMapper);
    }

    @Override
    public List<Contact> getAllContactsByUser(User user) throws PersistenceException {
        return getJdbcTemplate().query(READ_ALL_BY_USER, new Object[]{user.getId()}, contactRowMapper);
    }

    private PreparedStatement makeBasePrepareStatment(String query, Contact contact, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, contact.getLastName());
        statement.setString(2, contact.getFirstName());
        statement.setString(3, contact.getMiddleName());
        statement.setString(4, contact.getMobilePhone());
        statement.setString(5, contact.getHomePhone());
        statement.setString(6, contact.getAddress());
        statement.setString(7, contact.getEmail());
        statement.setLong(8, contact.getUser().getId());
        return statement;
    }
}
