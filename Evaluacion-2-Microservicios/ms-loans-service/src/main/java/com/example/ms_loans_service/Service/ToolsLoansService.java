package com.example.ms_loans_service.Service;

import com.example.ms_loans_service.Models.ToolsLoansModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ToolsLoansRemoteService {

    @Autowired
    private RestTemplate restTemplate;

    private final String INVENTORY_SERVICE_URL = "http://localhost:8082";

    // queryForObject - obtener relación herramienta-préstamo por ID
    public ToolsLoansModel getToolLoanById(Long id) {
        String url = INVENTORY_SERVICE_URL + "/api/tools-loans/" + id;
        return restTemplate.getForObject(url, ToolsLoansModel.class);
    }

    // queryForList - obtener herramientas de un préstamo
    public List<ToolsLoansModel> getToolLoansByLoanId(Long loanId) {
        String url = INVENTORY_SERVICE_URL + "/api/tools-loans/loan/" + loanId;
        ToolsLoansModel[] toolsLoans = restTemplate.getForObject(url, ToolsLoansModel[].class);
        return Arrays.asList(toolsLoans != null ? toolsLoans : new ToolsLoansModel[0]);
    }

    // Crear relación herramienta-préstamo
    public ToolsLoansModel saveToolLoan(ToolsLoansModel toolsLoansModel) {
        String url = INVENTORY_SERVICE_URL + "/api/tools-loans/";
        return restTemplate.postForObject(url, toolsLoansModel, ToolsLoansModel.class);
    }
}