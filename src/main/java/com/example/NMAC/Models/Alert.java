package com.example.NMAC.Models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "metric_type", nullable = false)
    private String metricType; // e.g., "Bandwidth", "Latency"

    @Column(name = "alert_message", nullable = false)
    private String alertMessage;

    @Column(name = "alert_timestamp", nullable = false)
    private LocalDateTime alertTimestamp;
}
