package com.kemane.microservice.controller;

import com.kemane.microservice.client.UserClient;
import com.kemane.microservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class UserController {

    @Autowired
    private UserClient userClient;

    @PostMapping(value = "/create")
    public ResponseEntity<User> saveClient(@RequestBody User user){
        return userClient.saveUser(user);
    }

}
