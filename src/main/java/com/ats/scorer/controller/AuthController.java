package com.ats.scorer.controller;

import com.ats.scorer.model.User;
import com.ats.scorer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        if (userRepository.existsByUsername(user.getUsername())) {
            response.put("success", false);
            response.put("message", "Username already exists.");
            return ResponseEntity.badRequest().body(response);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            response.put("success", false);
            response.put("message", "Email already registered.");
            return ResponseEntity.badRequest().body(response);
        }

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("CANDIDATE");
        }

        User saved = userRepository.save(user);
        response.put("success", true);
        response.put("message", "Registration successful!");
        response.put("user", saved);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User u = userOpt.get();
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("username", u.getUsername());
            response.put("fullName", u.getFullName());
            response.put("email", u.getEmail());
            response.put("role", u.getRole());
            return ResponseEntity.ok(response);
        }

        response.put("success", false);
        response.put("message", "Invalid username or password");
        return ResponseEntity.badRequest().body(response);
    }
}
