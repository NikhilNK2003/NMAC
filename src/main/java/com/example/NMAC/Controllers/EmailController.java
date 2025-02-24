package com.example.NMAC.Controllers;

import com.example.NMAC.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;  // Injecting EmailService

    @PostMapping("/send")
    public String sendTestEmail(@RequestBody Map<String, String> request) {
        String to = request.get("to");  // Extracting recipient email from JSON body

        if (to == null || to.isEmpty()) {
            return "Error: 'to' field is required!";
        }

        String subject = "Test Email from Spring Boot";
        String body = "Hello, this is a test email from your Spring Boot application!";

        emailService.sendEmail(to, subject, body);  // Calling the email service

        return "Email sent successfully to " + to;
    }
}
