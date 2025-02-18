package com.example.NMAC.Service;

import com.example.NMAC.Models.Metric;
import com.example.NMAC.Repository.MetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MetricService {

    @Autowired
    private MetricRepository metricRepository;

    // Add a new metric
    public Metric addMetric(Metric metric) {
        try {
            System.out.println("Adding metric: " + metric);
        metric.setTimestamp(LocalDateTime.now()); // Set current timestamp
        Metric savedMetric = metricRepository.save(metric);
        System.out.println("Saved metric: " + savedMetric);
        return savedMetric;
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error adding metric: " + e.getMessage());
            throw e;
        }
    }
//    public Metric addMetric(Metric metric) {
//        System.out.println("Adding metric: " + metric);
//        metric.setTimestamp(LocalDateTime.now()); // Set current timestamp
//        Metric savedMetric = metricRepository.save(metric);
//        System.out.println("Saved metric: " + savedMetric);
//        return savedMetric;
//    }
    // Get metrics by device ID
    public List<Metric> getMetricsByDevice(Long deviceId) {
        return metricRepository.findByDeviceId(deviceId);
    }
}

