package com.example.NMAC.Service;

import com.example.NMAC.Models.Alert;
import com.example.NMAC.Models.User;
import com.example.NMAC.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository; // Assuming you have a User table

    public void sendCriticalAlert(Alert alert) {
        List<User> admins = userRepository.findByRoles_Name("ADMIN");// Fetch admins
        List<String> adminEmails = admins.stream().map(User::getEmail).collect(Collectors.toList());

        if (!adminEmails.isEmpty()) {
            System.out.println("📧 Sending alert email to: " + adminEmails);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmails.toArray(new String[0]));
            message.setSubject("CRITICAL ALERT: " + alert.getMetricType());
            message.setText("Alert: " + alert.getAlertMessage() + "\nDevice: " + alert.getDevice().getDeviceName());
            message.setFrom("nmacminiproject@gmail.com");
            try {
                mailSender.send(message);
                System.out.println("✅ Email sent successfully.");
            } catch (Exception e) {
                System.err.println("❌ Error sending email: " + e.getMessage());
            }
        }else {
            System.out.println("⚠️ No admin emails found.");
        }
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("nmacminiproject@gmail.com"); // Must match `spring.mail.username`

        mailSender.send(message);
    }
}

