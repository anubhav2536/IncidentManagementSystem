package com.IMS.IncidentManagementSystem.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.IMS.IncidentManagementSystem.Repository.IncidentRepository;
import com.IMS.IncidentManagementSystem.Repository.UserRepository;
import com.IMS.IncidentManagementSystem.exception.InvalidOperationException;
import com.IMS.IncidentManagementSystem.exception.ResourceNotFoundException;
import com.IMS.IncidentManagementSystem.exception.UnauthorizedAccessException;
import com.IMS.IncidentManagementSystem.model.Incident;
import com.IMS.IncidentManagementSystem.model.IncidentStatus;
import com.IMS.IncidentManagementSystem.model.User;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private UserRepository userRepository;

    public Incident createIncident(Incident incident, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        incident.setUser(user);
        incident.setReportedDateTime(LocalDateTime.now());
        incident.setIncidentId(generateIncidentId());
        return incidentRepository.save(incident);
    }

    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id).orElse(null);
    }

    public Incident updateIncident(Long id, Incident updatedIncident, Long userId) {
        Optional<Incident> incidentOpt = incidentRepository.findById(id);
        if (incidentOpt.isPresent()) {
            Incident incident = incidentOpt.get();
            if (!incident.getStatus().equals(IncidentStatus.CLOSED) && incident.getUser().getId().equals(userId)) {
                incident.setType(updatedIncident.getType());
                incident.setDetails(updatedIncident.getDetails());
                incident.setPriority(updatedIncident.getPriority());
                incident.setStatus(updatedIncident.getStatus());
                return incidentRepository.save(incident);
            } else {
                throw new RuntimeException("Incident is closed does not have permission to edit.");
            }
        } else {
            throw new RuntimeException("Incident not found");
        }
    }

    public void deleteIncident(Long id, Long userId) {
        Incident existingIncident = incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found with id " + id));
        if (!existingIncident.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("User not authorized to delete this incident");
        }
        if (existingIncident.getStatus() == IncidentStatus.CLOSED) {
            throw new InvalidOperationException("Cannot delete a closed incident");
        }
        incidentRepository.delete(existingIncident);
    }

    private String generateIncidentId() {
        Random random = new Random();
        int randomNumber = random.nextInt(90000) + 10000; // 5-digit random number
        int year = LocalDateTime.now().getYear();
        return "RMG" + randomNumber + year;
    }

}