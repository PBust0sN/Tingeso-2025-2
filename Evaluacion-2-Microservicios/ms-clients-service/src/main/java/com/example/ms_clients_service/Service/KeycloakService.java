package com.example.ms_clients_service.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.util.*;

@Service
public class KeycloakService {

    //private static final String KEYCLOAK_URL = "http://localhost:8080";
    private static final String KEYCLOAK_URL = "http://keycloak:8080";  
    private static final String REALM = "toolRent";

    private static final String ADMIN_REALM = "master";
    private static final String ADMIN_CLIENT_ID = "admin-cli";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public String getAdminToken() {
        try {
            RestTemplate restTemplate = getRestTemplate();

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

            if (response.getBody() == null || !response.getBody().containsKey("access_token")) {
                throw new RuntimeException("No se pudo obtener el token de Keycloak");
            }

            return (String) response.getBody().get("access_token");
        } catch (RestClientException e) {
            System.err.println("Error al obtener token de Keycloak: " + e.getMessage());
            throw new RuntimeException("Error de conexión con Keycloak: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error inesperado en getAdminToken: " + e.getMessage());
            throw new RuntimeException("Error en Keycloak Service: " + e.getMessage(), e);
        }
    }

    public void createUserInKeycloak(String username, String email, String password, Long id_real, String role) {
        try {
            String token = getAdminToken();
            RestTemplate restTemplate = getRestTemplate();

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

            ResponseEntity<String> response = restTemplate.postForEntity(
                    KEYCLOAK_URL + "/admin/realms/" + REALM + "/users",
                    request,
                    String.class
            );

            // Obtener userId desde el header Location
            if (response.getHeaders().getLocation() != null) {
                String location = response.getHeaders().getLocation().toString();
                String userId = location.substring(location.lastIndexOf('/') + 1);
                // Asignar el rol al usuario
                assignRealmRoleToUser(userId, role);
            } else {
                throw new RuntimeException("No se pudo obtener el ID del usuario creado");
            }
        } catch (RestClientException e) {
            System.err.println("Error REST al crear usuario en Keycloak: " + e.getMessage());
            throw new RuntimeException("Error al crear usuario en Keycloak: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error inesperado en createUserInKeycloak: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error en creación de usuario Keycloak: " + e.getMessage(), e);
        }
    }

    public void assignRealmRoleToUser(String userId, String roleName) {
        try {
            String token = getAdminToken();
            RestTemplate restTemplate = getRestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Obtener información del rol del realm
            ResponseEntity<Map> roleResponse = restTemplate.exchange(
                    KEYCLOAK_URL + "/admin/realms/" + REALM + "/roles/" + roleName,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Map.class
            );

            if (roleResponse.getBody() == null) {
                throw new RuntimeException("No se encontró el rol: " + roleName);
            }

            Map<String, Object> roleInfo = roleResponse.getBody();

            // Debe enviarse en una LISTA
            List<Map<String, Object>> roles = List.of(roleInfo);

            HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(roles, headers);

            restTemplate.postForEntity(
                    KEYCLOAK_URL + "/admin/realms/" + REALM + "/users/" + userId + "/role-mappings/realm",
                    request,
                    String.class
            );
        } catch (Exception e) {
            System.err.println("Error al asignar rol en Keycloak: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al asignar rol: " + e.getMessage(), e);
        }
    }


}

