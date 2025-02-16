package com.example.NMAC.Models;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_name", nullable = false)
    private String deviceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    @Column(name = "ip_address", nullable = false, unique = true)
    private String ipAddress;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('Active', 'Inactive') DEFAULT 'Active'")
    private DeviceStatus status;
}

enum DeviceType {
    ROUTER, SWITCH, FIREWALL, SERVER
}

enum DeviceStatus {
    ACTIVE, INACTIVE
}


