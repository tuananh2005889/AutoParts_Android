// com.BackEnd.repository.PasswordResetTokenRepository.java
package com.BackEnd.repository;

import com.BackEnd.model.PasswordResetToken;
import com.BackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByUser(User user);

    Optional<PasswordResetToken> findByResetCode(String code);

    void deleteByUser(User user);
}
