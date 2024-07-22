package com.IMS.IncidentManagementSystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.IMS.IncidentManagementSystem.Repository.UserRepository;
import com.IMS.IncidentManagementSystem.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User authenticateUser(String userName, String password) {
        User user = userRepository.findByUserName(userName);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }

    public void changePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with the provided email");
        }
    }

    public User createUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}