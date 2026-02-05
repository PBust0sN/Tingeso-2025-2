package com.example.monolitico.Controller;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Service.ClientService;
import com.example.monolitico.Service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin("*")
public class ClientController {
    @Autowired
    ClientService clientService;

    @Autowired
    KeycloakService keycloakService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<ClientEntity>> getAllClients() {
        List<ClientEntity> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable Long id) {
        ClientEntity clientEntity = clientService.getClientById(id);
        return ResponseEntity.ok(clientEntity);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ClientEntity> saveClient(@RequestBody ClientEntity clientEntity) {
        ClientEntity newClient = clientService.saveClient(clientEntity);
        return ResponseEntity.ok(newClient);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/")
    public ResponseEntity<ClientEntity> updateClient(@RequestBody ClientEntity clientEntity) {
        ClientEntity updatedClient = clientService.updateClient(clientEntity);
        return ResponseEntity.ok(updatedClient);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ClientEntity> deleteClient(@PathVariable Long id) throws Exception {
        var isDeleted = clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/getbyrut/{rut}")
    public ResponseEntity<ClientEntity> getClientByRut(@PathVariable String rut){
        ClientEntity client = clientService.findByRut(rut);
        return ResponseEntity.ok(client);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        try {
            Map<String, String> tokens = keycloakService.loginUser(username, password);
            return ResponseEntity.ok(tokens);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }
}
