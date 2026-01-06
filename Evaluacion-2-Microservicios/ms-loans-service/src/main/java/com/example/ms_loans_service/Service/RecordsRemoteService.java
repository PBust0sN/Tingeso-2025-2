package com.example.ms_loans_service.Service;

import com.example.ms_loans_service.Models.RecordsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class RecordsRemoteService {

    @Autowired
    private RestTemplate restTemplate;

    private final String GATEWAY_URL = "https://gateway-service.default.svc.cluster.local:8433";
    private final String SERVICE_ENDPOINT = "/records";

    // Crear registro
    public RecordsModel saveRecord(RecordsModel recordsModel) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/";
        return restTemplate.postForObject(url, recordsModel, RecordsModel.class);
    }

    // queryForObject - obtener registro por ID
    public RecordsModel getRecordsById(Long id) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/" + id;
        return restTemplate.getForObject(url, RecordsModel.class);
    }

    // queryForList - obtener todos los registros
    public List<RecordsModel> getAllRecords() {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/";
        RecordsModel[] records = restTemplate.getForObject(url, RecordsModel[].class);
        return Arrays.asList(records != null ? records : new RecordsModel[0]);
    }

    // Actualizar registro
    public RecordsModel updateRecord(RecordsModel recordsModel) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/";
        restTemplate.put(url, recordsModel);
        return recordsModel;
    }

    // queryForList - obtener registros entre fechas
    public List<RecordsModel> findByRecordDatesBetween(LocalDate startDate, LocalDate endDate) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/dates?start=" + startDate + "&end=" + endDate;
        RecordsModel[] records = restTemplate.getForObject(url, RecordsModel[].class);
        return Arrays.asList(records != null ? records : new RecordsModel[0]);
    }
}