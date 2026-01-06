package com.example.ms_reports_service.Repository;

import com.example.ms_reports_service.Entities.LoansReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanReportRepository extends JpaRepository<LoansReportEntity, Long> {
    public List<LoansReportEntity> findByReportId(Long  reportId);
}
