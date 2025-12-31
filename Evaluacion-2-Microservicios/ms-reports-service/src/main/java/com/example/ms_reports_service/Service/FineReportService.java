package com.example.ms_reports_service.Service;

import com.example.ms_reports_service.Entities.FineReportEntity;
import com.example.ms_reports_service.Entities.ToolsRankingEntity;
import com.example.ms_reports_service.Repository.FineReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FineReportService {

    @Autowired
    private FineReportRepository fineReportRepository;

    //in this case we only gona implement create, getall and delete
    //because its a report, when save, we only can delete, not update

    public List<FineReportEntity> getAllFineReport() {
        return fineReportRepository.findAll();
    }

    public List<FineReportEntity> getFineReportsByReportId(Long reportId){
        return fineReportRepository.findByReportId(reportId);
    }

    public FineReportEntity createFineReport(FineReportEntity fineReportEntity) {
        return fineReportRepository.save(fineReportEntity);
    }

    public boolean deleteFineReportById(Long id) throws Exception {
        try{
            fineReportRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
