package com.phonebook.controller;

import com.phonebook.dao.PersistenceException;
import com.phonebook.model.User;
import com.phonebook.service.UserService;
import com.phonebook.service.ValidationService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Controller
public class AuthController {
    private static final Logger LOGGER = LogManager.getLogger(AuthController.class);

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserService userService;

    @RequestMapping("/signin")
    public String signin() {
        return "signin";
    }

    @RequestMapping("/registration/success")
    public String afterRegistration(Model model) {
        model.addAttribute("message", "Вы прошли регистрацию");
        return "forward:/signin";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationForm() {
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> registration(@RequestParam Map<String, String> parameters) {
        Map<String, String> result = null;
        try {
            User user = parseUserFromRequest(parameters);
            result = validationService.verifyUser(user, parameters.get("passwordconfirmation"));
            if (result.isEmpty()) {
                userService.save(user);
            }
        } catch (PersistenceException e) {
            result.put("messagefield", "Ошибка");
            LOGGER.error(e);
        }
        return result.isEmpty() ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }

    private User parseUserFromRequest(Map<String, String> parameters) {
        User user = new User();
        user.setName(parameters.get("name"));
        user.setLogin(parameters.get("login"));
        user.setPassword(parameters.get("password"));
        return user;
    }
}
