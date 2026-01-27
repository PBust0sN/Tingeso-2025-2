package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.FineReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineReportRepository extends JpaRepository<FineReportEntity, Long> {
    public List<FineReportEntity> findByReportId(Long  reportId);
}
