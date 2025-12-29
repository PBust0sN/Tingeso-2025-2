package com.example.ms_inventory_service.Service;

import com.example.ms_inventory_service.Models.LoansModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class LoansRemoteService {

    @Autowired
    private RestTemplate restTemplate;

    private final String GATEWAY_URL = "http://ms-loans-service:8080";
    private final String SERVICE_ENDPOINT = "/api/loans";

    // queryForObject - obtener préstamo por ID
    public LoansModel getLoanById(Long id) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/" + id;
        return restTemplate.getForObject(url, LoansModel.class);
    }

    // queryForList - obtener todos los préstamos
    public List<LoansModel> getAllLoans() {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/";
        LoansModel[] loans = restTemplate.getForObject(url, LoansModel[].class);
        return Arrays.asList(loans != null ? loans : new LoansModel[0]);
    }

    // Crear préstamo
    public LoansModel saveLoan(LoansModel loansModel) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/";
        return restTemplate.postForObject(url, loansModel, LoansModel.class);
    }
}