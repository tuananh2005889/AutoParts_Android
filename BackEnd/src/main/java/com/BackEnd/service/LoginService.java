package com.BackEnd.service;

import com.BackEnd.repository.LoginRepository;
import com.BackEnd.repository.UserRepository;
import com.BackEnd.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private LoginRepository loginRepository;
    private final UserRepository userRepo;

    public LoginService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void saveUser(User user) {
        loginRepository.save(user);
    }

    public List<User> getAllUser() {
        return loginRepository.findAll();
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByGmail(email);
    }

    public Optional<User> Login(String userName, String password) {
        return Optional.ofNullable(loginRepository.findByUserNameAndPassword(userName, password));
    }

    public User createUser(String fullName, String email) {
        User u = new User();
        u.setFullName(fullName);
        u.setGmail(email);
        u.setUserName(email);
        u.setPassword("");
        return userRepo.save(u);
    }
}
