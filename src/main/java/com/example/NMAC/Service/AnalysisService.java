package com.example.NMAC.Service;

import com.example.NMAC.Models.AnalysisResult;
import com.example.NMAC.Models.Device;
import com.example.NMAC.Models.Metric;
import com.example.NMAC.Repository.AnalysisResultRepository;
import com.example.NMAC.Repository.DeviceRepository;
import com.example.NMAC.Repository.MetricRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.OptionalDouble;

@Service
public class AnalysisService {

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private AnalysisResultRepository analysisResultRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @PostConstruct
    public void performInitialAnalysis() {
        System.out.println("Performing initial analysis on startup...");

        List<Device> devices = deviceRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Device device : devices) {
            for (String metricType : getAllMetricTypes()) {
                List<Metric> metrics = metricRepository.findByDeviceIdAndMetricType(device.getId(), metricType);

                if (!metrics.isEmpty()) {
                    calculateAndSaveAnalysis(metrics, device, metricType, now);
                }
            }
        }
    }


    @Scheduled(fixedRate = 300000) // 5 minutes (300000 ms)
    public void scheduleAnalysis() {
        System.out.println("Running scheduled analysis...");

        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minus(5, ChronoUnit.MINUTES);
        LocalDateTime now = LocalDateTime.now();

        List<Device> devices = deviceRepository.findAll();

        for (Device device : devices) {
            for (String metricType : getAllMetricTypes()) {
                List<Metric> metrics = metricRepository.findByDeviceIdAndMetricTypeAndTimestampAfter(
                        device.getId(), metricType, fiveMinutesAgo);

                if (!metrics.isEmpty()) {
                    calculateAndSaveAnalysis(metrics, device, metricType, now);
                }
            }
        }
    }

    // ✅ 3. Analyze a specific device's metrics (manual trigger)
    public AnalysisResult analyzeDeviceMetrics(Long deviceId, String metricType) {
        List<Metric> metrics = metricRepository.findByDeviceIdAndMetricType(deviceId, metricType);

        if (metrics.isEmpty()) return null;

        return calculateAndSaveAnalysis(metrics, metrics.get(0).getDevice(), metricType, LocalDateTime.now());
    }

    // 4. Get all analysis results
    public List<AnalysisResult> getAllResults() {
        return analysisResultRepository.findAll();
    }

    // 5. Calculate and save analysis
    private AnalysisResult calculateAndSaveAnalysis(List<Metric> metrics, Device device, String metricType, LocalDateTime timestamp) {
        OptionalDouble average = metrics.stream().mapToDouble(Metric::getValue).average();
        double maxValue = metrics.stream().mapToDouble(Metric::getValue).max().orElse(0);

        AnalysisResult result = new AnalysisResult();
        result.setDevice(device);
        result.setMetricType(metricType);
        result.setAverageValue(average.orElse(0));
        result.setMaxValue(maxValue);
        result.setTimestamp(timestamp);

        analysisResultRepository.save(result);
        System.out.println("Analysis saved for " + device.getDeviceName() + " - " + metricType);
        return result;
    }


    private List<String> getAllMetricTypes() {
        return List.of("Bandwidth", "Latency", "Packet Loss", "Port Utilization", "Throughput", "Error Rate",
                "Blocked Requests", "Intrusion Attempts", "CPU Load", "CPU Usage", "Memory Usage", "Disk I/O");
    }
}
