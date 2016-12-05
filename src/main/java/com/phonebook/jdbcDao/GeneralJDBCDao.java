package com.phonebook.jdbcDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;


public class GeneralJDBCDao extends JdbcDaoSupport {

    private static final String GENERATED_KEY_NAME = System.getProperty("GENERATED_KEY_NAME");

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initializer() {
        setDataSource(dataSource);
    }

    protected Long parseLongFromHolder(KeyHolder holder) {
        Long id;
        Object tmp = holder.getKeys().get(GENERATED_KEY_NAME);
        id = tmp instanceof Long ? (Long) tmp : Long.valueOf(((Integer) tmp).longValue());
        return id;
    }
}
