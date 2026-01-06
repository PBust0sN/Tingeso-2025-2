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

    private final String GATEWAY_URL = "https://gateway-service.default.svc.cluster.local:8433";
    private final String SERVICE_ENDPOINT = "/tools-loans";

    // queryForObject - obtener relación herramienta-préstamo por ID
    public ToolsLoansModel getToolLoanById(Long id) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/" + id;
        return restTemplate.getForObject(url, ToolsLoansModel.class);
    }

    // queryForList - obtener herramientas de un préstamo
    public List<ToolsLoansModel> getToolLoansByLoanId(Long loanId) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/loan/" + loanId;
        ToolsLoansModel[] toolsLoans = restTemplate.getForObject(url, ToolsLoansModel[].class);
        return Arrays.asList(toolsLoans != null ? toolsLoans : new ToolsLoansModel[0]);
    }

    // Crear relación herramienta-préstamo
    public ToolsLoansModel saveToolLoan(ToolsLoansModel toolsLoansModel) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/";
        return restTemplate.postForObject(url, toolsLoansModel, ToolsLoansModel.class);
    }
}