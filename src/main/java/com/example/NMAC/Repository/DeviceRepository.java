package com.example.NMAC.Repository;

import com.example.NMAC.Models.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    // Custom query to find a device by IP address
    Device findByIpAddress(String ipAddress);
}
