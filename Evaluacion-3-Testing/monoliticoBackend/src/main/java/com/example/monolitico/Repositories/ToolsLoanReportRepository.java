package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ToolsLoanReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolsLoanReportRepository extends JpaRepository<ToolsLoanReportEntity,Long> {
    @Query(value = "SELECT tool_id FROM tools_loan_report WHERE loan_id = :loanId", nativeQuery = true)
    public List<Long> findToolIdByLoanId(Long loanId);
}
