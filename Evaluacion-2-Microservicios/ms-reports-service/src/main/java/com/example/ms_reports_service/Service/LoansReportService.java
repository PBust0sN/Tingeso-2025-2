package com.example.ms_reports_service.Service;

import com.example.ms_reports_service.Entities.LoansReportEntity;
import com.example.ms_reports_service.Repository.LoanReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoansReportService {
    @Autowired
    private LoanReportRepository loanReportRepository;

    public List<LoansReportEntity> getAllLoansReport(){
        return loanReportRepository.findAll();
    }

    public List<LoansReportEntity> getLoansReportByReportId(Long reportId){
        return loanReportRepository.findByReportId(reportId);
    }

    public LoansReportEntity getLoansReportById(Long reportId){
        return loanReportRepository.getById(reportId);
    }

    public LoansReportEntity createLoansReport(LoansReportEntity loansReportEntity){
        return loanReportRepository.save(loansReportEntity);
    }

    public boolean deleteLoansReportById(Long id) throws Exception{
        try{
            loanReportRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
