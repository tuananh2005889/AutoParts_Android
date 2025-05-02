package com.BackEnd.service;

import com.BackEnd.dto.UpdateUserInfoRequest;
import com.BackEnd.dto.UserDTO;
import com.BackEnd.model.PasswordResetToken;
import com.BackEnd.model.User;
import com.BackEnd.repository.PasswordResetTokenRepository;
import com.BackEnd.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordResetTokenRepository tokenRepo;
    private final JavaMailSender mailSender;

    public UserService(UserRepository userRepo,
            PasswordResetTokenRepository tokenRepo,
            JavaMailSender mailSender) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.mailSender = mailSender;
    }

    private UserDTO toDto(User u) {
        UserDTO dto = new UserDTO();
        dto.setUserId(u.getUserId());
        dto.setUserName(u.getUserName());
        dto.setFullName(u.getFullName());
        dto.setGmail(u.getGmail());
        dto.setPhone(u.getPhone());
        dto.setAddress(u.getAddress());
        dto.setRole(u.getRole());
        dto.setAvatarUrl(u.getAvatarUrl());
        return dto;
    }

    public void initiateForgotPassword(String gmail) {
        User user = userRepo.findByGmail(gmail)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        // Xoá token cũ nếu có
        tokenRepo.findByUser(user).ifPresent(tokenRepo::delete);

        // Sinh mã 4 số
        String code = String.valueOf(1000 + new Random().nextInt(9000));

        // Tạo và lưu token mới
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setResetCode(code);
        token.setExpiry(LocalDateTime.now().plusMinutes(10));
        tokenRepo.save(token);

        // Gửi mail
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(gmail);
        msg.setSubject("Password Reset Code");
        msg.setText("Your reset code is: " + code + "\nIt expires in 10 minutes.");
        mailSender.send(msg);
    }

    public boolean verifyResetCode(String gmail, String code) {
        Optional<User> ou = userRepo.findByGmail(gmail);
        if (ou.isEmpty())
            return false;

        return tokenRepo.findByUser(ou.get())
                .filter(t -> t.getResetCode().equals(code) &&
                        t.getExpiry().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public void resetPassword(String gmail, String newPassword) {
        User u = userRepo.findByGmail(gmail)
                .orElseThrow(() -> new RuntimeException("Email not found"));
        System.out.println("DEBUG: changing password for " + gmail + " → " + newPassword);
        u.setPassword(newPassword);
        userRepo.save(u);
    }

    public Optional<UserDTO> getUserByuserName(String userName) {
        return userRepo.findByUserName(userName)
                .map(this::toDto);
    }

    public boolean updateAvatar(String userName, String avatarUrl) {
        return userRepo.findByUserName(userName)
                .map(u -> {
                    u.setAvatarUrl(avatarUrl);
                    userRepo.save(u);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public UserDTO updateUserInfo(UpdateUserInfoRequest dto) {
        User u = userRepo.findByUserName(dto.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserName()));

        u.setFullName(dto.getFullName());
        u.setGmail(dto.getGmail());
        // nếu password không rỗng thì cập nhật
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            u.setPassword(dto.getPassword());
        }
        u.setPhone(dto.getPhone());
        u.setAddress(dto.getAddress());
        User saved = userRepo.save(u);
        return toDto(saved);
    }
}
