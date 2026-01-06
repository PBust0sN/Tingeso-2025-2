package com.example.ms_reports_service.Repository;

import com.example.ms_reports_service.Entities.ToolsRankingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolsRankingRepository extends JpaRepository<ToolsRankingEntity, Long> {
    public List<ToolsRankingEntity> findByReportId(Long reportId);
}
