package com.phonebook;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


@SpringBootApplication
public class Application {
    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        systemVariablesSetUp();
        SpringApplication.run(Application.class, args);
    }

    private static void systemVariablesSetUp() {
        String path = System.getProperty("lardi.conf");

        if (path == null) {
            System.setProperty("GENERATED_KEY_NAME", "id");
            return;
        }

        Properties properties = new Properties();
        InputStream stream = null;
        try {
            stream = new FileInputStream(path);
            properties.load(stream);
        } catch (FileNotFoundException e) {
            LOGGER.info(e);
        } catch (IOException e) {
            LOGGER.info(e);
        }

        for (Map.Entry<Object, Object> entry : properties.entrySet())
            System.setProperty((String) entry.getKey(), (String) entry.getValue());

        String profile = properties.getProperty("mysql");
        if (profile != null) {
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
            if (profile.equals("mysql")) System.setProperty("GENERATED_KEY_NAME", "GENERATED_KEY");
        }
    }
}