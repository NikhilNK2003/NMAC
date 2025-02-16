package com.example.NMAC.Controllers;

import com.example.NMAC.Models.AnalysisResult;
import com.example.NMAC.Service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    // Analyze metrics for a device
    @GetMapping("/{deviceId}/{metricType}")
    public AnalysisResult analyzeDeviceMetrics(@PathVariable Long deviceId, @PathVariable String metricType) {
        return analysisService.analyzeDeviceMetrics(deviceId, metricType);
    }
}

