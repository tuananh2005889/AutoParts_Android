package com.BackEnd.controller;

import com.BackEnd.service.LoginService;
import com.BackEnd.config.JwtUtil;
import com.BackEnd.dto.GoogleLoginRequest;
import com.BackEnd.dto.GoogleLoginResponse;
import com.BackEnd.model.User;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jdbc.support.JdbcUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;
// JWT
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// Google ID token
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Value("${google.clientId}")
    private String googleClientId;

    private final JwtUtil jwtUtil;

    public LoginController(LoginService loginService, JwtUtil jwtUtil) {
        this.loginService = loginService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        if (user.getUserName() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Missing required fields!");

        }
        System.out.println("Received user daata: " + user);
        loginService.saveUser(user);
        return ResponseEntity.ok("Successfully signed up!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIN(@RequestBody User userss) {
        Optional<User> user = loginService.Login(userss.getUserName(), userss.getPassword());
        return user.map(value -> ResponseEntity.ok("Login successful!"))
                .orElseGet(() -> ResponseEntity.status(401).body("Invalid username or password"));
    }

    @PostMapping("/google")
    public ResponseEntity<GoogleLoginResponse> loginWithGoogle(@RequestBody GoogleLoginRequest req) {
        try {
            // Xác thực ID token với Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(req.getIdToken());
            if (idToken == null) {
                // token không hợp lệ
                return ResponseEntity
                        .status(HttpStatus.SC_UNAUTHORIZED)
                        .body(null);
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            User user = loginService.findByEmail(email)
                    .orElseGet(() -> loginService.createUser(name, email));

            String jwt = jwtUtil.generateToken(user.getUserName(), user.getUserId());

            GoogleLoginResponse response = new GoogleLoginResponse(
                    user.getUserId(),
                    jwt,
                    user.getUserName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Bắt exception chung
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}