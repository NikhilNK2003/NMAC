package com.example.NMAC.Service;

import com.example.NMAC.Models.Alert;
import com.example.NMAC.Models.Metric;
import com.example.NMAC.Repository.MetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MetricService {

    @Autowired
    private MetricRepository metricRepository;
    @Autowired
    private AlertService alertService;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // Add a new metric and notify SSE clients
    public Metric addMetric(Metric metric) {
        try {
            metric.setTimestamp(LocalDateTime.now()); // Set current timestamp
            Metric savedMetric = metricRepository.save(metric);

            // Trigger an alert if latency is high
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

            // 🔹 Notify all clients about new data
            notifyClients(savedMetric);

            return savedMetric;
        } catch (Exception e) {
            System.err.println("Error adding metric: " + e.getMessage());
            throw e;
        }
    }

    // Register an SSE client
    public SseEmitter addEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    // Notify all clients with new metric data
    private void notifyClients(Metric metric) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(metric);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
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
