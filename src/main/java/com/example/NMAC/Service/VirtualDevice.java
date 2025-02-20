//package com.example.NMAC.Service;
//
//import com.example.NMAC.Models.Device;
//import com.example.NMAC.Models.Metric;
//import com.example.NMAC.Repository.DeviceRepository;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Random;
//
//@Service
//public class VirtualDevice {
//
//    private final MetricService metricService; // Use MetricService instead of MetricRepository
//    private final DeviceRepository deviceRepository; // Fetch devices from DB
//    private final Random random = new Random();
//
//    public VirtualDevice(MetricService metricService, DeviceRepository deviceRepository) {
//        this.metricService = metricService;
//        this.deviceRepository = deviceRepository;
//    }
//
//    @Scheduled(fixedRate = 10000) // Runs every 10 seconds
//    public void generateMetrics() {
//        List<Device> devices = deviceRepository.findAll(); // Fetch devices from DB
//        if (devices.isEmpty()) {
//            System.out.println("No devices found. Skipping metric generation.");
//            return;
//        }
//
//        for (Device device : devices) {
//            double latency = random.nextDouble() * 200; // Random latency value
//            Metric metric = new Metric(
//                    null, // Auto-generated ID
//                    device,
//                    "Latency",
//                    latency,
//                    LocalDateTime.now()
//            );
//
//            // 🔹 Use MetricService to add the metric (will also generate alerts)
//            metricService.addMetric(metric);
//
//            System.out.println("Generated metric for " + device.getDeviceName() + ": " + latency + "ms");
//        }
//    }
//}
