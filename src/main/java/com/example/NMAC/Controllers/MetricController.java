package com.example.NMAC.Controllers;

import com.example.NMAC.Models.Metric;
import com.example.NMAC.Service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metrics")
@CrossOrigin(origins = "http://localhost:3000/")
public class MetricController {

    @Autowired
    private MetricService metricService;

    // Add a new metric
    @PostMapping
    public Metric addMetric(@RequestBody Metric metric) {
        return metricService.addMetric(metric);
    }

    // Get metrics by device ID
    @GetMapping("/{deviceId}")
    public List<Metric> getMetricsByDevice(@PathVariable Long deviceId) {
        return metricService.getMetricsByDevice(deviceId);
    }
}
