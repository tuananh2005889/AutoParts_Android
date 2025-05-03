package com.BackEnd.controller;

import com.BackEnd.dto.UserDTO;
import com.BackEnd.dto.VerifyCodeRequest;
import com.BackEnd.dto.ForgotPasswordRequest;
import com.BackEnd.dto.ResetPasswordRequest;
import com.BackEnd.dto.UpdateUserInfoRequest;
import com.BackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/name/{userName}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String userName) {
        return userService.getUserByuserName(userName)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/update-avatar")
    public ResponseEntity<String> updateAvatar(
            @RequestParam String userName,
            @RequestParam String avatarUrl) {

        boolean updated = userService.updateAvatar(userName, avatarUrl);
        if (updated) {
            return ResponseEntity.ok("Avatar updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequest req) {
        userService.initiateForgotPassword(req.getGmail());
        return ResponseEntity.ok("Password reset code sent to email");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(
            @RequestBody VerifyCodeRequest req) {
        boolean valid = userService.verifyResetCode(req.getGmail(), req.getCode());
        if (valid) {
            return ResponseEntity.ok("Valid code");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired code");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest req) {
        userService.resetPassword(req.getGmail(), req.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @PutMapping("/update-info")
    public ResponseEntity<UserDTO> updateInfo(@RequestBody UpdateUserInfoRequest dto) {
        UserDTO updated = userService.updateUserInfo(dto);
        return ResponseEntity.ok(updated);
    }
}
