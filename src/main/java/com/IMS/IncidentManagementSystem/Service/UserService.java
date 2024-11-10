package com.IMS.IncidentManagementSystem.Service;

import com.IMS.IncidentManagementSystem.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.IMS.IncidentManagementSystem.Repository.UserRepository;
import com.IMS.IncidentManagementSystem.model.User;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User authenticateUser(String userName, String password) {
        User user = userRepository.findByUserName(userName);
       // System.out.println(user.toString());
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

    public String createUser(User user) {
        if(userRepository.findByEmail(user.getEmail())!=null){
            return "user already exits";
        }
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully!";
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
    public Set<Incident> getIncidents(String userName){
        return userRepository.findByUserName(userName).getIncidents();
    }
}