package com.IMS.IncidentManagementSystem.Controllers;

import com.IMS.IncidentManagementSystem.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.IMS.IncidentManagementSystem.Service.UserService;
import com.IMS.IncidentManagementSystem.model.ForgotPasswordRequest;
import com.IMS.IncidentManagementSystem.model.LoginRequest;
import com.IMS.IncidentManagementSystem.model.User;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("react is trying");
       // System.out.println(loginRequest.getUserName()+" "+loginRequest.getPassword());
        try {
            User user = userService.authenticateUser(loginRequest.getUserName(), loginRequest.getPassword());
           // System.out.println("react is trying +1");
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            //System.out.println(e);
            return ResponseEntity.badRequest().body(null); // Adjust as needed for your error handling
        }
    }
    @GetMapping("/getAllIncidents/{userName}")
    public Set<Incident> getAllIncidents(@PathVariable String userName){
        return userService.getIncidents(userName);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        try {
            userService.changePassword(forgotPasswordRequest.getEmail(), forgotPasswordRequest.getNewPassword());
            return ResponseEntity.ok("Password reset successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build(); // Adjust as needed for your error handling
        }
    }

    @PostMapping("/register")
    public String createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/username/{userName}")
    public User getUserByUserName(@PathVariable String userName) {
        return userService.getUserByUserName(userName);
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }
}