package com.example.ms_reports_service.Service;

import com.example.ms_reports_service.Models.ClientModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ClientRemoteService {

    @Autowired
    private RestTemplate restTemplate;

    private final String GATEWAY_URL = "http://ms-clients-service:8081";
    private final String SERVICE_ENDPOINT = "/api/clients";

    // queryForObject - obtener cliente por ID
    public ClientModel getClientById(Long id) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/" + id;
        return restTemplate.getForObject(url, ClientModel.class);
    }

    // queryForList - obtener todos los clientes
    public List<ClientModel> getAllClients() {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/";
        ClientModel[] clients = restTemplate.getForObject(url, ClientModel[].class);
        return Arrays.asList(clients != null ? clients : new ClientModel[0]);
    }

    // Validar si cliente tiene préstamos expirados
    public boolean hasExpiredLoansById(Long id) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/" + id + "/hasExpiredLoans";
        return Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class));
    }
}