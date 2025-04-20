package com.BackEnd.repository;

import com.BackEnd.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<User, Long> {
    User findByUserNameAndPassword(String userName, String password);
}
