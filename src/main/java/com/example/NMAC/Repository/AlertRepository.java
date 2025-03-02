package com.example.NMAC.Repository;

import com.example.NMAC.Models.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository <Alert, Long> {
    List<Alert> findByDeviceId(Long deviceId);
}
