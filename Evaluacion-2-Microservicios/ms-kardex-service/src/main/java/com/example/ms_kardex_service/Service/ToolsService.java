package com.example.ms_kardex_service.Service;

import com.example.ms_kardex_service.Models.ToolsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ToolsRemoteService {

    @Autowired
    private RestTemplate restTemplate;

    private final String GATEWAY_URL = "http://ms-inventory-service:8082";
    private final String SERVICE_ENDPOINT = "/api/tools";

    // queryForObject - obtener herramienta por ID
    public ToolsModel getToolById(Long id) {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/" + id;
        return restTemplate.getForObject(url, ToolsModel.class);
    }

    // queryForList - obtener todas las herramientas
    public List<ToolsModel> getAllTools() {
        String url = GATEWAY_URL + SERVICE_ENDPOINT + "/";
        ToolsModel[] tools = restTemplate.getForObject(url, ToolsModel[].class);
        return Arrays.asList(tools != null ? tools : new ToolsModel[0]);
    }
}