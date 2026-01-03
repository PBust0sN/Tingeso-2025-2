package com.example.ms_inventory_service.Service;

import com.example.ms_inventory_service.Models.RecordsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.List;

@Service
public class RecordsRemoteService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String RECORDS_SERVICE_URL = "https://gateway-service.default.svc.cluster.local:8433/records";

    public List<RecordsModel> getAllRecords() {
        try {
            RecordsModel[] records = restTemplate.getForObject(RECORDS_SERVICE_URL + "/", RecordsModel[].class);
            return List.of(records != null ? records : new RecordsModel[0]);
        } catch (Exception e) {
            System.err.println("Error obteniendo registros: " + e.getMessage());
            return List.of();
        }
    }

    public RecordsModel saveRecord(RecordsModel record) {
        try {
            return restTemplate.postForObject(RECORDS_SERVICE_URL + "/", record, RecordsModel.class);
        } catch (Exception e) {
            System.err.println("Error guardando registro: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public RecordsModel getRecordById(Long id) {
        try {
            return restTemplate.getForObject(RECORDS_SERVICE_URL + "/{id}", RecordsModel.class, id);
        } catch (Exception e) {
            System.err.println("Error obteniendo registro por ID: " + e.getMessage());
            return null;
        }
    }

    public RecordsModel updateRecord(RecordsModel record) {
        try {
            restTemplate.put(RECORDS_SERVICE_URL + "/", record);
            return record;
        } catch (Exception e) {
            System.err.println("Error actualizando registro: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteRecord(Long id) {
        try {
            restTemplate.delete(RECORDS_SERVICE_URL + "/{id}", id);
            return true;
        } catch (Exception e) {
            System.err.println("Error eliminando registro: " + e.getMessage());
            return false;
        }
    }

    public List<RecordsModel> findByRecordDateBetween(Date startDate, Date endDate) {
        try {
            RecordsModel[] records = restTemplate.getForObject(
                    RECORDS_SERVICE_URL + "/between?start={start}&end={end}",
                    RecordsModel[].class,
                    startDate,
                    endDate
            );
            return List.of(records != null ? records : new RecordsModel[0]);
        } catch (Exception e) {
            System.err.println("Error obteniendo registros entre fechas: " + e.getMessage());
            return List.of();
        }
    }
}
