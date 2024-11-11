package com.IMS.IncidentManagementSystem.Controllers;

import com.IMS.IncidentManagementSystem.Service.EmailService;
import com.IMS.IncidentManagementSystem.Service.UserService;
import com.IMS.IncidentManagementSystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.IMS.IncidentManagementSystem.Service.IncidentService;
import com.IMS.IncidentManagementSystem.model.Incident;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @PostMapping
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident, @RequestParam String userName) {

       User user = userService.getUserByUserName(userName);
        //System.out.println(user.getUserName());
        emailService.sendIncidentNotification(
                "example@gmail.com", // Change this to the recipient's email
                incident.getTitle(),
                incident.getDescription()
        );

        return ResponseEntity.ok(incidentService.createIncident(incident, user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getIncidentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Incident> updateIncident(@PathVariable Long id, @RequestBody Incident updatedIncident,
            @RequestParam Long userId) {
        return ResponseEntity.ok(incidentService.updateIncident(id, updatedIncident, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id, @RequestParam Long userId) {
        incidentService.deleteIncident(id, userId);
        return ResponseEntity.noContent().build();
    }
}