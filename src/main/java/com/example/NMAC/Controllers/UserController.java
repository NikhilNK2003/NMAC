package com.example.NMAC.Controllers;

import com.example.NMAC.Models.Role;
import com.example.NMAC.Models.User;
import com.example.NMAC.Repository.RoleRepository;
import com.example.NMAC.Repository.UserRepository;
import com.example.NMAC.Service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExcelService excelService;

    // Register a new user (Admin-only access)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/adduser")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");
        String password = request.get("password");
        String roleName = request.get("role"); // Example: "ADMIN", "ANALYST", "VIEWER"

        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Invalid role: " + roleName));

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        newUser.setRoles(roles);

        userRepository.save(newUser);

        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    // Get all users (Admin-only access)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allusers")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get a specific user by username (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{username}")
    public Optional<User> getUserByUsername(@PathVariable String username) {
        return userRepository.findByUsername(username);
    }

    // Update user password (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updatepassword")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String newPassword = request.get("newPassword");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }

    // Delete a user by username (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }

        userRepository.delete(userOptional.get());
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }
    // Upload Excel file to add users
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload-users")
    public ResponseEntity<?> uploadUsers(@RequestParam("file") MultipartFile file) {
        List<String> messages = excelService.processExcelFile(file);
        return ResponseEntity.ok(messages);
    }
}
