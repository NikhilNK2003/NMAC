package com.example.NMAC.Service;

import com.example.NMAC.Models.Device;
import com.example.NMAC.Models.DeviceType;
import com.example.NMAC.Models.Metric;
import com.example.NMAC.Repository.DeviceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class VirtualDevice {

    private final MetricService metricService;
    private final DeviceRepository deviceRepository;
    private final Random random = new Random();

    public VirtualDevice(MetricService metricService, DeviceRepository deviceRepository) {
        this.metricService = metricService;
        this.deviceRepository = deviceRepository;
    }

    @Scheduled(fixedRate = 30000) // Runs every 30 seconds
    public void generateMetrics() {
        List<Device> devices = deviceRepository.findAll();
        if (devices.isEmpty()) {
            System.out.println("No devices found. Skipping metric generation.");
            return;
        }

        for (Device device : devices) {
            if ("Active".equalsIgnoreCase(String.valueOf(device.getStatus()))) {
                generateDeviceMetrics(device);
            }
        }

    }

    private void generateDeviceMetrics(Device device) {
        switch (device.getDeviceType()) {
            case ROUTER -> generateRouterMetrics(device);
            case SWITCH -> generateSwitchMetrics(device);
            case FIREWALL -> generateFirewallMetrics(device);
            case SERVER -> generateServerMetrics(device);
        }
    }

    private void generateRouterMetrics(Device device) {
        saveMetric(device, "Bandwidth", random.nextDouble() * 1000); // Mbps
        saveMetric(device, "Latency", random.nextDouble() * 200); // ms
        saveMetric(device, "Packet Loss", random.nextDouble() * 10); // %
    }

    private void generateSwitchMetrics(Device device) {
        saveMetric(device, "Port Utilization", random.nextDouble() * 100); // %
        saveMetric(device, "Throughput", random.nextDouble() * 500); // Mbps
        saveMetric(device, "Error Rate", random.nextDouble() * 5); // %
    }

    private void generateFirewallMetrics(Device device) {
        saveMetric(device, "Blocked Requests", random.nextInt(1000)); // Count
        saveMetric(device, "Intrusion Attempts", random.nextInt(50)); // Count
        saveMetric(device, "CPU Load", random.nextDouble() * 100); // %
    }

    private void generateServerMetrics(Device device) {
        saveMetric(device, "CPU Usage", random.nextDouble() * 100); // %
        saveMetric(device, "Memory Usage", random.nextDouble() * 64); // GB
        saveMetric(device, "Disk I/O", random.nextDouble() * 500); // MB/s
    }

    private void saveMetric(Device device, String metricName, double value) {
        Metric metric = new Metric(
                null, // Auto-generated ID
                device,
                metricName,
                value,
                LocalDateTime.now()
        );
        metricService.addMetric(metric);
        System.out.println("Generated " + metricName + " for " + device.getDeviceName() + ": " + value);
    }
}
