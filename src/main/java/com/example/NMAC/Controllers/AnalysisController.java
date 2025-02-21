package com.example.NMAC.Controllers;

import com.example.NMAC.Models.AnalysisResult;
import com.example.NMAC.Service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analysis")
@CrossOrigin(origins = "http://localhost:3000/")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    // Analyze metrics for a device
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @GetMapping("/analyst/{deviceId}/{metricType}")
    public AnalysisResult analyzeDeviceMetrics(@PathVariable Long deviceId, @PathVariable String metricType) {
        return analysisService.analyzeDeviceMetrics(deviceId, metricType);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    @GetMapping("/analyst")
    public List<AnalysisResult> getallresults() {
        return analysisService.getallreults();
    }
}

