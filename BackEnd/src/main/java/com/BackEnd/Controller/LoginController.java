package com.BackEnd.Controller;

import com.BackEnd.Service.LoginService;
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
    private LoginService  loginService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        if (user.getGmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Missing required fields!");
        }
        loginService.saveUser(user);
        return ResponseEntity.ok("Successfully signed up!");
    }


//    @PostMapping("/logIn")
//    public ResponseEntity<String> logIN(@RequestBody String gmail, @RequestBody String password) {
//        Optional<User> user = loginService.Login(gmail,password)  ;
//        return user.map(value -> ResponseEntity.ok(value.toString())).orElseGet(() -> ResponseEntity.status(401).body("Invalid gmail or password"));
//    }
}
