package com.example.ms_reports_service.Repository;

import com.example.ms_reports_service.Entities.FineReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineReportRepository extends JpaRepository<FineReportEntity, Long> {
    public List<FineReportEntity> findByReportId(Long  reportId);
}
