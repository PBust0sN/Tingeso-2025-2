package com.example.monolitico.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class KeycloakService {

    private static final String KEYCLOAK_URL = "http://localhost:8080";
    //private static final String KEYCLOAK_URL = "http://keycloak:8080";
    private static final String REALM = "toolRent";

    private static final String ADMIN_REALM = "master";
    private static final String ADMIN_CLIENT_ID = "admin-cli";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    public String getAdminToken() {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", ADMIN_CLIENT_ID);
        params.add("username", ADMIN_USERNAME);
        params.add("password", ADMIN_PASSWORD);
        params.add("grant_type", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                KEYCLOAK_URL + "/realms/" + ADMIN_REALM + "/protocol/openid-connect/token",
                new HttpEntity<>(params, headers),
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }

    public void createUserInKeycloak(String username, String email, String password, Long id_real) {
        String token = getAdminToken();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", password);
        credential.put("temporary", false);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id_real", List.of(id_real.toString()));

        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("enabled", true);
        user.put("credentials", List.of(credential));
        user.put("attributes", attributes);


        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

        restTemplate.postForEntity(
                KEYCLOAK_URL + "/admin/realms/" + REALM + "/users",
                request,
                String.class
        );
    }
}

