package com.example.ms_reports_service.Service;

import com.example.ms_reports_service.Entities.ToolsReportEntity;
import com.example.ms_reports_service.Repository.ToolsReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolsReportService {

    @Autowired
    private ToolsReportRepository toolsReportRepository;

    public ToolsReportEntity getByToolId(Long id){
        return toolsReportRepository.findById(id).get();
    }

    public ToolsReportEntity createToolReport(ToolsReportEntity toolsReportEntity){
        return toolsReportRepository.save(toolsReportEntity);
    }
}
