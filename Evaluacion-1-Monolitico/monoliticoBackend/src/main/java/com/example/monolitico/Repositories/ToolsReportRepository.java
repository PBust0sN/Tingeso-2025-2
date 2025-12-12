package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ToolsReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolsReportRepository extends JpaRepository<ToolsReportEntity,Long> {
}
