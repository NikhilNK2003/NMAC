package com.example.NMAC.Repository;

import com.example.NMAC.Models.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findByDeviceId(Long deviceId);
}

