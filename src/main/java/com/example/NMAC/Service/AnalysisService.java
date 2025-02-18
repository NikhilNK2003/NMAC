package com.example.NMAC.Service;

import com.example.NMAC.Models.AnalysisResult;
import com.example.NMAC.Models.Metric;
import com.example.NMAC.Repository.AnalysisResultRepository;
import com.example.NMAC.Repository.MetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;

@Service
public class AnalysisService {

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private AnalysisResultRepository analysisResultRepository;

    // Analyze metrics and store results
    public AnalysisResult analyzeDeviceMetrics(Long deviceId, String metricType) {
        List<Metric> metrics = metricRepository.findByDeviceIdAndMetricType(deviceId, metricType);
        OptionalDouble average = metrics.stream().mapToDouble(Metric::getValue).average();
        double maxValue = metrics.stream().mapToDouble(Metric::getValue).max().orElse(0);

        AnalysisResult result = new AnalysisResult();
        result.setDevice(metrics.get(0).getDevice());
        result.setMetricType(metricType);
        result.setAverageValue(average.orElse(0));
        result.setMaxValue(maxValue);
        result.setTimestamp(LocalDateTime.now());
        return analysisResultRepository.save(result);
    }

    public List<AnalysisResult> getallreults() {
        return analysisResultRepository.findAll();
    }
}





