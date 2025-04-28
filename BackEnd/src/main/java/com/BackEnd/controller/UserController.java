package com.BackEnd.controller;

import com.BackEnd.dto.UserDTO;
import com.BackEnd.dto.CartDTO;
import com.BackEnd.model.User;
import com.BackEnd.model.Cart;
import com.BackEnd.repository.UserRepository;
import com.BackEnd.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/name/{userName}")
    public ResponseEntity<UserDTO> getUserName(@PathVariable String userName) {
        return userService.getUserByuserName(userName)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/update-avatar")
    public ResponseEntity<String> updateAvatar(
            @RequestParam String userName,
            @RequestParam String avatarUrl) {

        boolean updated = userService.updateAvatar(userName, avatarUrl);
        return updated
                ? ResponseEntity.ok("Avatar updated successfully")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}
