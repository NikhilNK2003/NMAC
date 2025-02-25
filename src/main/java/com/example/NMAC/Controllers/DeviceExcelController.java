package com.example.NMAC.Controllers;

import com.example.NMAC.Models.Device;
import com.example.NMAC.Service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceExcelController {

    @Autowired
    private ExcelService excelService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/uploadExcel")
    public ResponseEntity<?> uploadDevicesFromExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty. Please upload a valid Excel file.");
        }

        try {
            List<Device> savedDevices = excelService.saveDevicesFromExcel(file);
            return ResponseEntity.ok(savedDevices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing Excel file: " + e.getMessage());
        }
    }
}
