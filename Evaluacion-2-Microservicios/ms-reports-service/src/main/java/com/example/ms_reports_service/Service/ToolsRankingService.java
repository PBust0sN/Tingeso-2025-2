package com.example.ms_reports_service.Service;

import com.example.ms_reports_service.Entities.ToolsRankingEntity;
import com.example.ms_reports_service.Repository.ToolsRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolsRankingService {

    @Autowired
    private ToolsRankingRepository toolsRankingRepository;

    public List<ToolsRankingEntity> getAllToolsRanking(){
        return toolsRankingRepository.findAll();
    }

    public List<ToolsRankingEntity> getToolsRankingByReportId(Long reportId){
        return toolsRankingRepository.findByReportId(reportId);
    }

    public ToolsRankingEntity createToolsRanking(ToolsRankingEntity toolsRankingEntity){
        return toolsRankingRepository.save(toolsRankingEntity);
    }

    public boolean deleteToolsRankingById(Long id)throws  Exception{
        try{
            toolsRankingRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
