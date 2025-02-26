package com.example.NMAC.Models;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "metrics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false, foreignKey = @ForeignKey(name = "fk_metric_device", foreignKeyDefinition = "FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE"))
    private Device device;


    @Column(name = "metric_type", nullable = false)
    private String metricType; // e.g., "Bandwidth", "Latency"

    @Column(name = "value", nullable = false)
    private double value;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;



}
