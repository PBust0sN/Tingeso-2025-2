package com.example.ms_reports_service.Service;

import com.example.ms_reports_service.Entities.ToolsLoanReportEntity;
import com.example.ms_reports_service.Repository.ToolsLoanReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolsLoanReportService {

    @Autowired
    private ToolsLoanReportRepository toolsLoanReportRepository;

    public ToolsLoanReportEntity  createToolsLoanReport(ToolsLoanReportEntity toolsLoanReportEntity){
        return toolsLoanReportRepository.save(toolsLoanReportEntity);
    }

    public List<Long> findByLoanId(Long id){
        return toolsLoanReportRepository.findToolIdByLoanId(id);
    }
}
