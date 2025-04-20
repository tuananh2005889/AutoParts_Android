package com.BackEnd.controller;

import com.BackEnd.service.LoginService;
import com.BackEnd.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        if (user.getUserName() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Missing required fields!");

        }
        System.out.println("Received user daata: " + user);
        loginService.saveUser(user);
        return ResponseEntity.ok("Successfully signed up!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIN(@RequestBody User userss) {
        Optional<User> user = loginService.Login(userss.getUserName(), userss.getPassword());
        return user.map(value -> ResponseEntity.ok("Login successful!"))
                .orElseGet(() -> ResponseEntity.status(401).body("Invalid username or password"));
    }

}