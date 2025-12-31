package com.example.ms_reports_service.Service;

import com.example.ms_reports_service.Models.FineModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class FineRemoteService {

    @Autowired
    private RestTemplate restTemplate;

    private final String GATEWAY_URL = "http://ms-rates-service:8083";
    private final String SERVICE_ENDPOINT = "/api/fines";

    // queryForObject - obtener multa por ID
    public FineModel getFineById(Long id) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/" + id;
        return restTemplate.getForObject(url, FineModel.class);
    }

    // queryForList - obtener todas las multas
    public List<FineModel> getAllFines() {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/";
        FineModel[] fines = restTemplate.getForObject(url, FineModel[].class);
        return Arrays.asList(fines != null ? fines : new FineModel[0]);
    }

    // queryForList - obtener multas por cliente
    public List<FineModel> getFinesByClientId(Long clientId) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/client/" + clientId;
        FineModel[] fines = restTemplate.getForObject(url, FineModel[].class);
        return Arrays.asList(fines != null ? fines : new FineModel[0]);
    }
}