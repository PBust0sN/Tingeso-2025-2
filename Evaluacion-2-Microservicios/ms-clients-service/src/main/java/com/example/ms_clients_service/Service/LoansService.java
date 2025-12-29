package com.example.ms_clients_service.Service;

import com.example.ms_clients_service.Models.LoansModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class LoansRemoteService {

    @Autowired
    private RestTemplate restTemplate;

    private final String LOANS_SERVICE_URL = "http://localhost:8080";

    // queryForObject - obtener préstamo por ID
    public LoansModel getLoanById(Long id) {
        String url = LOANS_SERVICE_URL + "/api/loans/" + id;
        return restTemplate.getForObject(url, LoansModel.class);
    }

    // queryForList - obtener todos los préstamos
    public List<LoansModel> getAllLoans() {
        String url = LOANS_SERVICE_URL + "/api/loans/";
        LoansModel[] loans = restTemplate.getForObject(url, LoansModel[].class);
        return Arrays.asList(loans != null ? loans : new LoansModel[0]);
    }

    // queryForList - obtener préstamos por cliente
    public List<LoansModel> getLoansByClientId(Long clientId) {
        String url = LOANS_SERVICE_URL + "/api/loans/client/" + clientId;
        LoansModel[] loans = restTemplate.getForObject(url, LoansModel[].class);
        return Arrays.asList(loans != null ? loans : new LoansModel[0]);
    }

    // Crear préstamo
    public LoansModel saveLoan(LoansModel loansModel) {
        String url = LOANS_SERVICE_URL + "/api/loans/";
        return restTemplate.postForObject(url, loansModel, LoansModel.class);
    }
}