package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ToolsRankingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolsRankingRepository extends JpaRepository<ToolsRankingEntity, Long> {
    public List<ToolsRankingEntity> findByReportId(Long reportId);
}
