package com.example.NMAC.Repository;

import com.example.NMAC.Models.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnalysisReportRepository extends JpaRepository<AnalysisResult, Long> {
    List<AnalysisResult> findAll();
}
