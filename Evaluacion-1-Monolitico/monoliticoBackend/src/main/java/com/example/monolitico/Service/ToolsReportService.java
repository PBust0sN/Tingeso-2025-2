package com.example.monolitico.Service;

import com.example.monolitico.Entities.ToolsReportEntity;
import com.example.monolitico.Repositories.ToolsReportRepository;
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
