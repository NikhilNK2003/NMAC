package com.example.NMAC.Service;

import com.example.NMAC.Models.Alert;
import com.example.NMAC.Models.Metric;
import com.example.NMAC.Repository.MetricRepository;
import com.example.NMAC.Service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MetricService {

    @Autowired
    private MetricRepository metricRepository;
    @Autowired
    private AlertService alertService;

    // Add a new metric
    public Metric addMetric(Metric metric) {
        try {

            metric.setTimestamp(LocalDateTime.now()); // Set current timestamp
            Metric savedMetric = metricRepository.save(metric);
            if ("Latency".equals(metric.getMetricType()) && metric.getValue() > 100) {
                Alert alert = new Alert(
                        null,  // ID is auto-generated
                        metric.getDevice(),
                        "Latency",
                        "High latency detected: " + metric.getValue() + "ms",
                        LocalDateTime.now()
                );
                alertService.saveAlert(alert);
            }
        return savedMetric;
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error adding metric: " + e.getMessage());
            throw e;
        }
    }

    // Get metrics by device ID
    public List<Metric> getMetricsByDevice(Long deviceId) {
        return metricRepository.findByDeviceId(deviceId);
    }

    public List<Metric> getallmetrics() {
        return metricRepository.findAll();
    }
}

