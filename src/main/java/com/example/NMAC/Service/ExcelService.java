package com.example.NMAC.Service;

import com.example.NMAC.Models.*;
import com.example.NMAC.Repository.DeviceRepository;
import com.example.NMAC.Repository.RoleRepository;
import com.example.NMAC.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExcelService {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

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
                    String statusStr = row.getCell(4).getStringCellValue();
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

    public List<String> processExcelFile(MultipartFile file) {
        List<String> messages = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                String username = row.getCell(0).getStringCellValue();
                String email = row.getCell(1).getStringCellValue();
                String password = row.getCell(2).getStringCellValue();
                String roleName = row.getCell(3).getStringCellValue();

                if (userRepository.findByUsername(username).isPresent()) {
                    messages.add("Username already exists: " + username);
                    continue;
                }

                if (userRepository.findByEmail(email).isPresent()) {
                    messages.add("Email already exists: " + email);
                    continue;
                }

                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Invalid role: " + roleName));

                User newUser = new User();
                newUser.setUsername(username);
                newUser.setEmail(email);
                newUser.setPassword(passwordEncoder.encode(password));
                newUser.setRoles(Set.of(role));

                userRepository.save(newUser);
                messages.add("User added: " + username);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing file: " + e.getMessage());
        }

        return messages;
    }
}
