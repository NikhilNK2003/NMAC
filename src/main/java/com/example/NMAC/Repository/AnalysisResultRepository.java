package com.example.NMAC.Repository;

import com.example.NMAC.Models.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {

    // Custom method to find AnalysisResult by deviceId and metricType
    List<AnalysisResult> findByDeviceIdAndMetricType(Long deviceId, String metricType);
}
