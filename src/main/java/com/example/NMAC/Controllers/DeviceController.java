package com.example.NMAC.Controllers;

import com.example.NMAC.Models.Device;
import com.example.NMAC.Service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
@CrossOrigin(origins = "http://localhost:3000/")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public Device addDevice(@RequestBody Device device) {
        return deviceService.addDevice(device);
    }

    // Add multiple devices
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/addMultiple")
    public List<Device> addDevices(@RequestBody List<Device> devices) {
        return deviceService.addDevices(devices);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')") // ✅ Fixed
    @GetMapping("/viewer")
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/admin/{id}")
//    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
//        return deviceService.getDeviceById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/ip/{ipAddress}")
//    public ResponseEntity<Device> getDeviceByIp(@PathVariable String ipAddress) {
//        Device device = deviceService.getDeviceByIp(ipAddress);
//        return device != null ? ResponseEntity.ok(device) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
@PreAuthorize("hasRole('ADMIN')")
@PutMapping("/admin/{id}")
public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody Device editedDevice) {
    try {
        Device updatedDevice = deviceService.updateDevice(id, editedDevice);
        return ResponseEntity.ok(updatedDevice);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}



    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
