package com.example.NMAC.Repository;

import com.example.NMAC.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByRoles_Name(String roleName);  // ✅ Correct (matches roles.name)

    Optional<Object> findByEmail(String email);
}
