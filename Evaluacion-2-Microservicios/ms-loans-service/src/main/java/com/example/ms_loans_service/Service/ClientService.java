package com.example.ms_loans_service.Service;

import com.example.ms_loans_service.Models.ClientModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ClientRemoteService {

    @Autowired
    private RestTemplate restTemplate;

    private final String CLIENTS_SERVICE_URL = "http://localhost:8081";

    // queryForObject - obtener un cliente por ID
    public ClientModel getClientById(Long id) {
        String url = CLIENTS_SERVICE_URL + "/api/clients/" + id;
        return restTemplate.getForObject(url, ClientModel.class);
    }

    // queryForList - obtener todos los clientes
    public List<ClientModel> getAllClients() {
        String url = CLIENTS_SERVICE_URL + "/api/clients/";
        ClientModel[] clients = restTemplate.getForObject(url, ClientModel[].class);
        return Arrays.asList(clients != null ? clients : new ClientModel[0]);
    }

    // Validar si cliente tiene préstamos expirados
    public boolean hasExpiredLoansById(Long id) {
        String url = CLIENTS_SERVICE_URL + "/api/clients/" + id + "/hasExpiredLoans";
        return Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class));
    }

    // queryForList - obtener todos los préstamos de un cliente
    public List<Long> findAllLoansByClientId(Long clientId) {
        String url = CLIENTS_SERVICE_URL + "/api/clients/" + clientId + "/loans";
        Long[] loans = restTemplate.getForObject(url, Long[].class);
        return Arrays.asList(loans != null ? loans : new Long[0]);
    }

    // Validar si cliente tiene la misma herramienta en préstamo
    public boolean HasTheSameToolInLoanByClientId(Long clientId, Long toolId) {
        String url = CLIENTS_SERVICE_URL + "/api/clients/" + clientId + "/hasToolInLoan/" + toolId;
        return Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class));
    }

    // Actualizar información del cliente
    public ClientModel updateClient(ClientModel clientModel) {
        String url = CLIENTS_SERVICE_URL + "/api/clients/";
        restTemplate.put(url, clientModel);
        return clientModel;
    }

    // Buscar cliente por RUT
    public ClientModel findByRut(String rut) {
        String url = CLIENTS_SERVICE_URL + "/api/clients/rut/" + rut;
        return restTemplate.getForObject(url, ClientModel.class);
    }
}