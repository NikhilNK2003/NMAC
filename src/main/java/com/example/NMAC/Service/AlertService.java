package com.example.NMAC.Service;

import com.example.NMAC.Models.Alert;
import com.example.NMAC.Repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    // Save a new alert
    public Alert saveAlert(Alert alert) {
        alert.setAlertTimestamp(LocalDateTime.now());
        return alertRepository.save(alert);
    }

    // Get alerts by device
    public List<Alert> getAlertsByDevice(Long deviceId) {
        return alertRepository.findByDeviceId(deviceId);
    }

    public List<Alert> getallalerts() {
        return alertRepository.findAll();
    }
}

