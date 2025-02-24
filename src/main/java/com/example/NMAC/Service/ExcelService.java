package com.example.NMAC.Service;

import com.example.NMAC.Models.Device;
import com.example.NMAC.Models.DeviceType;
import com.example.NMAC.Models.DeviceStatus;
import com.example.NMAC.Repository.DeviceRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> saveDevicesFromExcel(MultipartFile file) {
        List<Device> devices = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0); // First sheet
            Iterator<Row> rowIterator = sheet.iterator();
            boolean firstRow = true; // Skip header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (firstRow) {
                    firstRow = false;
                    continue; // Skip header
                }

                Device device = new Device();
                device.setDeviceName(row.getCell(0).getStringCellValue());
                device.setIpAddress(row.getCell(1).getStringCellValue());

                // Convert string to enum for DeviceType
                String deviceTypeStr = row.getCell(2).getStringCellValue().toUpperCase();
                DeviceType deviceType = DeviceType.valueOf(deviceTypeStr);
                device.setDeviceType(deviceType);

                // If location is present
                if (row.getCell(3) != null) {
                    device.setLocation(row.getCell(3).getStringCellValue());
                }

                // Convert string to enum for DeviceStatus
                if (row.getCell(4) != null) {
                    String statusStr = row.getCell(4).getStringCellValue().toUpperCase();
                    DeviceStatus status = DeviceStatus.valueOf(statusStr);
                    device.setStatus(status);
                }

                devices.add(device);
            }

            return deviceRepository.saveAll(devices);
        } catch (IOException e) {
            throw new RuntimeException("Error processing Excel file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid enum value in Excel file: " + e.getMessage());
        }
    }
}
