package com.example.NMAC.Controllers;

import com.example.NMAC.Models.Device;
import com.example.NMAC.Service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/devices")
@CrossOrigin(origins = "http://localhost:3000/")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    // Post request to add a new device
    @PostMapping
    public Device addDevice(@RequestBody Device device) {
        return deviceService.addDevice(device);
    }

    // Get all devices
    @GetMapping
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }

    // Get device by ID
    @GetMapping("/{id}")
    public Optional<Device> getDeviceById(@PathVariable Long id) {
        return deviceService.getDeviceById(id);
    }

    // Get device by IP Address
    @GetMapping("/ip/{ipAddress}")
    public Device getDeviceByIp(@PathVariable String ipAddress) {
        return deviceService.getDeviceByIp(ipAddress);
    }

    // Delete device
    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
    }
}

