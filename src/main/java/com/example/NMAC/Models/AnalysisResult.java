package com.example.NMAC.Models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "metric_type", nullable = false)
    private String metricType; // e.g., "Bandwidth", "Latency"

    @Column(name = "average_value")
    private Double averageValue;

    @Column(name = "max_value")
    private Double maxValue;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
