package com.example.NMAC.Controllers;

import com.example.NMAC.Models.Role;
import com.example.NMAC.Models.User;
import com.example.NMAC.Repository.RoleRepository;
import com.example.NMAC.Repository.UserRepository;
import com.example.NMAC.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000/")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

//        String role = userDetails.getRoles().getName();

        String token = jwtUtil.generateToken(user);

        return Map.of("token", token);
    }


    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email"); // ✅ Get email from request
        String password = request.get("password");
        String roleName = request.get("role"); // "ADMIN", "ANALYST", "VIEWER"

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists"); // ✅ Check duplicate email
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Invalid role: " + roleName));

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email); // ✅ Set email before saving
        newUser.setPassword(passwordEncoder.encode(password));

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        newUser.setRoles(roles);

        userRepository.save(newUser);

        return Map.of("message", "User registered successfully");
    }

}


