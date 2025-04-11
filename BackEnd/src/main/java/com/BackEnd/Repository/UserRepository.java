package com.BackEnd.Repository;

import com.BackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserName(String userName);
}
