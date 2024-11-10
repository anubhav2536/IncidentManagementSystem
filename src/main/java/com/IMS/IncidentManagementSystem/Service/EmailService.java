package com.IMS.IncidentManagementSystem.Service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

    @Service
    public class EmailService {

        @Autowired
        private JavaMailSender emailSender;

        public void sendIncidentNotification(String recipientEmail, String incidentTitle, String incidentDescription) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("New Incident Created: " + incidentTitle);
            message.setText("A new incident has been reported with the following details:\n\n"
                    + "Title: " + incidentTitle + "\n"
                    + "Description: " + incidentDescription + "\n\n"
                    + "Please review and take necessary action.");

            emailSender.send(message);
        }
    }


