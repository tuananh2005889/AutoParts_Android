package com.BackEnd.service;

import com.BackEnd.dto.UserDTO;
import com.BackEnd.model.User;
import com.BackEnd.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<UserDTO> getUserByuserName(String userName) {
        return userRepo.findByUserName(userName).map(this::toDto);
    }

    /* CẬP NHẬT AVATAR */
    public boolean updateAvatar(String userName, String avatarUrl) {
        return userRepo.findByUserName(userName)
                .map(u -> {
                    u.setAvatarUrl(avatarUrl);
                    userRepo.save(u);
                    return true;
                })
                .orElse(false);
    }

    /* MAP ENTITY → DTO */
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
}