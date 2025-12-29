package com.example.ms_reports_service.Repository;

import com.example.ms_reports_service.Entities.ToolsReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolsReportRepository extends JpaRepository<ToolsReportEntity,Long> {
}
