package com.example.NMAC.Controllers;

import com.example.NMAC.Models.Alert;
import com.example.NMAC.Models.AnalysisResult;
import com.example.NMAC.Service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@CrossOrigin(origins = "http://localhost:3000/")
public class AlertController {

    @Autowired
    private AlertService alertService;

    // Save an alert
    @PostMapping
    public Alert saveAlert(@RequestBody Alert alert) {
        return alertService.saveAlert(alert);
    }

    // Get alerts for a device
    @GetMapping("/{deviceId}")
    public List<Alert> getAlertsByDevice(@PathVariable Long deviceId) {
        return alertService.getAlertsByDevice(deviceId);
    }
    @GetMapping("/all devices")
    public List<Alert> getallresults() {
        return alertService.getallalerts();
    }
}
