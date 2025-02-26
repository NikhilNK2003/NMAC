package com.example.NMAC.Service;

import com.example.NMAC.Models.Alert;
import com.example.NMAC.Models.AlertSeverity;
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
    @Autowired
    private EmailService emailService;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // Add a new metric and notify SSE clients
    public Metric addMetric(Metric metric) {
        try {
            metric.setTimestamp(LocalDateTime.now()); // Set current timestamp
            Metric savedMetric = metricRepository.save(metric);

            // 🔥 Check and trigger alerts for all parameters
            checkAndTriggerAlert(metric);

            // 🔹 Notify all clients about new data
            notifyClients(savedMetric);

            return savedMetric;
        } catch (Exception e) {
            System.err.println("Error adding metric: " + e.getMessage());
            throw e;
        }
    }
    private void checkAndTriggerAlert(Metric metric) {
        String metricType = metric.getMetricType();
        double value = metric.getValue();
        String alertMessage = null;
        AlertSeverity severity = AlertSeverity.MINOR;

        switch (metricType) {
            case "Latency" -> {
                if (value > 150) {
                    alertMessage = "CRITICAL: Extremely high latency detected: " + value + "ms";
                    severity = AlertSeverity.CRITICAL;
                } else if (value > 100) {
                    alertMessage = "MAJOR: High latency detected: " + value + "ms";
                    severity = AlertSeverity.WARNING;
                }
            }
            case "Bandwidth" -> {
                if (value < 20) {
                    alertMessage = "CRITICAL: Very low bandwidth detected: " + value + " Mbps";
                    severity = AlertSeverity.CRITICAL;
                } else if (value < 50) {
                    alertMessage = "MAJOR: Low bandwidth detected: " + value + " Mbps";
                    severity = AlertSeverity.WARNING;
                }
            }
            case "Packet Loss" -> {
                if (value > 10) {
                    alertMessage = "CRITICAL: Severe packet loss detected: " + value + "%";
                    severity = AlertSeverity.CRITICAL;
                } else if (value > 5) {
                    alertMessage = "MAJOR: High packet loss detected: " + value + "%";
                    severity = AlertSeverity.WARNING;
                }
            }
            case "Port Utilization" -> {
                if (value > 90) {
                    alertMessage = "CRITICAL: Port utilization extremely high: " + value + "%";
                    severity = AlertSeverity.CRITICAL;
                } else if (value > 80) {
                    alertMessage = "MAJOR: High port utilization detected: " + value + "%";
                    severity = AlertSeverity.WARNING;
                }
            }
            case "Throughput" -> {
                if (value < 20) {
                    alertMessage = "CRITICAL: Very low throughput detected: " + value + " Mbps";
                    severity = AlertSeverity.CRITICAL;
                } else if (value < 50) {
                    alertMessage = "MAJOR: Low throughput detected: " + value + " Mbps";
                    severity = AlertSeverity.WARNING;
                }
            }
            case "Error Rate" -> {
                if (value > 5) {
                    alertMessage = "CRITICAL: Very high error rate detected: " + value + "%";
                    severity = AlertSeverity.CRITICAL;
                } else if (value > 2) {
                    alertMessage = "MAJOR: High error rate detected: " + value + "%";
                    severity = AlertSeverity.WARNING;
                }
            }
            case "Blocked Requests" -> {
                if (value > 800) {
                    alertMessage = "CRITICAL: Excessive blocked requests detected: " + value;
                    severity = AlertSeverity.CRITICAL;
                } else if (value > 500) {
                    alertMessage = "MAJOR: High number of blocked requests: " + value;
                    severity = AlertSeverity.WARNING;
                }
            }
            case "Intrusion Attempts" -> {
                if (value > 40) {
                    alertMessage = "CRITICAL: Multiple critical intrusion attempts detected: " + value;
                    severity = AlertSeverity.CRITICAL;
                } else if (value > 20) {
                    alertMessage = "MAJOR: Multiple intrusion attempts detected: " + value;
                    severity = AlertSeverity.WARNING;
                }
            }
            case "CPU Load", "CPU Usage" -> {
                if (value > 95) {
                    alertMessage = "CRITICAL: CPU load critically high: " + value + "%";
                    severity = AlertSeverity.CRITICAL;
                } else if (value > 90) {
                    alertMessage = "MAJOR: High CPU load detected: " + value + "%";
                    severity = AlertSeverity.WARNING;
                }
            }
            case "Memory Usage" -> {
                if (value > 60) {
                    alertMessage = "CRITICAL: Very high memory usage: " + value + " GB";
                    severity = AlertSeverity.CRITICAL;
                } else if (value > 50) {
                    alertMessage = "MAJOR: High memory usage detected: " + value + " GB";
                    severity = AlertSeverity.WARNING;
                }
            }
            case "Disk I/O" -> {
                if (value > 500) {
                    alertMessage = "CRITICAL: High disk I/O detected: " + value + " MB/s";
                    severity = AlertSeverity.CRITICAL;
                } else if (value > 400) {
                    alertMessage = "MAJOR: Increased disk I/O detected: " + value + " MB/s";
                    severity = AlertSeverity.WARNING;
                }
            }
        }

        // Save the alert if any condition is met
        if (alertMessage != null) {
            Alert alert = new Alert(null, metric.getDevice(), metricType, alertMessage, LocalDateTime.now(), severity);
            alertService.saveAlert(alert);

//            if (severity == AlertSeverity.CRITICAL) {
//                emailService.sendCriticalAlert(alert);
//            }
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
