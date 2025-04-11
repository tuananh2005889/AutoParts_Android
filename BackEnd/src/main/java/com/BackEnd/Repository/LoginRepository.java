package com.BackEnd.Repository;

import com.BackEnd.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<User, Long> {
    User findByUserNameAndPassword(String userName, String password);
}
