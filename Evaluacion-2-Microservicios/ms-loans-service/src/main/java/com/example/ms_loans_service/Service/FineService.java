package com.example.ms_loans_service.Service;

import com.example.ms_loans_service.Models.FineModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class FineRemoteService {

    @Autowired
    private RestTemplate restTemplate;

    private final String RATES_SERVICE_URL = "http://localhost:8083";

    // queryForObject - obtener multa por ID
    public FineModel getFineById(Long id) {
        String url = RATES_SERVICE_URL + "/api/fines/" + id;
        return restTemplate.getForObject(url, FineModel.class);
    }

    // queryForList - obtener todas las multas
    public List<FineModel> getAllFines() {
        String url = RATES_SERVICE_URL + "/api/fines/";
        FineModel[] fines = restTemplate.getForObject(url, FineModel[].class);
        return Arrays.asList(fines != null ? fines : new FineModel[0]);
    }

    // queryForList - obtener multas por cliente
    public List<FineModel> getAllFinesByClientId(Long clientId) {
        String url = RATES_SERVICE_URL + "/api/fines/client/" + clientId;
        FineModel[] fines = restTemplate.getForObject(url, FineModel[].class);
        return Arrays.asList(fines != null ? fines : new FineModel[0]);
    }

    // Crear multa
    public FineModel saveFine(FineModel fineModel) {
        String url = RATES_SERVICE_URL + "/api/fines/";
        return restTemplate.postForObject(url, fineModel, FineModel.class);
    }

    // Actualizar multa
    public FineModel updateFine(FineModel fineModel) {
        String url = RATES_SERVICE_URL + "/api/fines/";
        restTemplate.put(url, fineModel);
        return fineModel;
    }

    // Validar si cliente tiene multas
    public boolean hasFinesByClientId(Long clientId) {
        List<FineModel> fines = getAllFinesByClientId(clientId);
        return !fines.isEmpty();
    }
}