package com.example.ms_rates_service.Service;

import com.example.ms_rates_service.Models.ClientModel;
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

    // queryForObject - obtener cliente por ID
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

    // Actualizar cliente
    public ClientModel updateClient(ClientModel clientModel) {
        String url = CLIENTS_SERVICE_URL + "/api/clients/";
        restTemplate.put(url, clientModel);
        return clientModel;
    }
}