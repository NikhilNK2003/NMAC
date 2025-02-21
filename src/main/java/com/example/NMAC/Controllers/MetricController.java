package com.example.NMAC.Controllers;

import com.example.NMAC.Models.Metric;
import com.example.NMAC.Service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/metrics")
@CrossOrigin(origins = "http://localhost:3000/")
public class MetricController {

    @Autowired
    private MetricService metricService;

    // Add a new metric
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    @PostMapping("/viewer")
    public Metric addMetric(@RequestBody Metric metric) {
        return metricService.addMetric(metric);
    }

    // Get metrics by device ID
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    @GetMapping("/viewer/{deviceId}")
    public List<Metric> getMetricsByDevice(@PathVariable Long deviceId) {
        return metricService.getMetricsByDevice(deviceId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    @GetMapping("/viewer")
    public List<Metric> getMetrics() {
        return metricService.getallmetrics();
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    @GetMapping(value = "/viewer/stream", produces = "text/event-stream")
    public SseEmitter streamMetrics() {
        return metricService.addEmitter();
    }
}
