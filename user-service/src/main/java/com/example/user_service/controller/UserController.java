package com.example.user_service.controller;

import com.example.user_service.config.JwtUtils;
import com.example.user_service.entity.User;
import com.example.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> req) {
        User user = userService.register(req.get("username"), req.get("password"));
        Map<String, Object> res = new HashMap<>();
        res.put("message", "User registered successfully");
        res.put("userId", user.getId());
        return res;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.get("username"), req.get("password")));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtUtils.generateToken(req.get("username"));
        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        res.put("expiresIn", 3600);
        return res;
    }

    @GetMapping("/balance")
    public Map<String, Object> balance(@RequestParam String username) {
        User user = userService.findByUsername(username).orElseThrow();
        Map<String, Object> res = new HashMap<>();
        res.put("username", user.getUsername());
        res.put("balance", user.getBalance());
        return res;
    }

    @PostMapping("/update-balance")
    public ResponseEntity<?> updateBalance(@RequestBody Map<String, Object> req) {
        String username = (String) req.get("username");
        Double amount = ((Number) req.get("amount")).doubleValue();

        boolean updated = userService.updateBalance(username, amount);
        if (updated) {
            return ResponseEntity.ok().body(Map.of("message", "Balance updated"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }
    }

}
