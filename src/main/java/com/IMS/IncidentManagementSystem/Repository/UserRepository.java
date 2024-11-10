package com.IMS.IncidentManagementSystem.Repository;

import com.IMS.IncidentManagementSystem.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.IMS.IncidentManagementSystem.model.User;
import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
   // List<Incident> getAllIncidents()
    User findByEmail(String email);
}