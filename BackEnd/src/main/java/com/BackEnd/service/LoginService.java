package com.BackEnd.service;

import com.BackEnd.repository.LoginRepository;
import com.BackEnd.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private LoginRepository loginRepository;

    public void saveUser(User user) {
        loginRepository.save(user);
    }

    public List<User> getAllUser() {
        return loginRepository.findAll();
    }

    public Optional<User> Login(String userName, String password) {
        return Optional.ofNullable(loginRepository.findByUserNameAndPassword(userName, password));
    }

}
