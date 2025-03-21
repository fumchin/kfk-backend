// 此類別負責用戶相關的業務邏輯，例如用戶註冊、查詢及更新餘額。
package com.example.user_service.service;

import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String username, String password) {
        // 此方法用於創建新用戶，並初始化餘額為 10000。
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .balance(10000.0)
                .build();
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        // 此方法根據用戶名查詢用戶。
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByUserId(Long userId) {
        // 此方法根據用戶 ID 查詢用戶。
        return userRepository.findById(userId);
    }

    public boolean updateBalance(Long userId, Double amount) {
        // 此方法用於更新用戶餘額。
        Optional<User> userOpt = userRepository.findByUserId(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setBalance(user.getBalance() + amount);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
}
