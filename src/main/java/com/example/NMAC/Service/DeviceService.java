package com.example.NMAC.Service;

import com.example.NMAC.Models.Device;
import com.example.NMAC.Repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    // Add a new device
    public Device addDevice(Device device) {
        return deviceRepository.save(device);
    }

    // Get all devices

      public List<Device> getAllDevices() {
        try {
            return deviceRepository.findAll();
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error fetching devices: " + e.getMessage());
            throw e;
        }
    }

    // Get device by ID
    public Optional<Device> getDeviceById(Long id) {
        return deviceRepository.findById(id);
    }

    // Get device by IP Address
    public Device getDeviceByIp(String ipAddress) {
        return deviceRepository.findByIpAddress(ipAddress);
    }

    // Delete device by ID
    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    public Device updateDevice(Long id, Device editedDevice) {
        Optional<Device> existingDeviceOpt = deviceRepository.findById(id);
        if (existingDeviceOpt.isPresent()) {
            Device existingDevice = existingDeviceOpt.get();
            existingDevice.setDeviceName(editedDevice.getDeviceName());
            existingDevice.setIpAddress(editedDevice.getIpAddress());
            existingDevice.setDeviceType(editedDevice.getDeviceType());
            return deviceRepository.save(existingDevice);
        } else {
            throw new RuntimeException("Device not found");
        }
    }

}
