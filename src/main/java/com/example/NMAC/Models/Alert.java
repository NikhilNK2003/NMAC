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
    @JoinColumn(name = "device_id", nullable = false, foreignKey = @ForeignKey(name = "fk_alert_device", foreignKeyDefinition = "FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE"))
    private Device device;


    @Column(name = "metric_type", nullable = false)
    private String metricType;

    @Column(name = "alert_message", nullable = false)
    private String alertMessage;

    @Column(name = "alert_timestamp", nullable = false)
    private LocalDateTime alertTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private AlertSeverity severity;


}

