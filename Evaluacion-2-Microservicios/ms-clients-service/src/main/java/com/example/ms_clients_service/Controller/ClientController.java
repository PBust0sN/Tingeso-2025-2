package com.example.ms_clients_service.Controller;

import com.example.ms_clients_service.Entities.ClientEntity;
import com.example.ms_clients_service.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@CrossOrigin("*")
public class ClientController {
    @Autowired
    ClientService clientService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK - ms-clients-service is running");
    }

    
    @GetMapping("/")
    public ResponseEntity<List<ClientEntity>> getAllClients() {
            List<ClientEntity> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable Long id) {
        ClientEntity clientEntity = clientService.getClientById(id);
        return ResponseEntity.ok(clientEntity);
    }

    @PostMapping("/")
    public ResponseEntity<ClientEntity> saveClient(@RequestBody ClientEntity clientEntity) {
        ClientEntity newClient = clientService.saveClient(clientEntity);
        return ResponseEntity.ok(newClient);
    }

    @PutMapping("/")
    public ResponseEntity<ClientEntity> updateClient(@RequestBody ClientEntity clientEntity) {
        ClientEntity updatedClient = clientService.updateClient(clientEntity);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClientEntity> deleteClient(@PathVariable Long id) throws Exception {
        var isDeleted = clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getbyrut/{rut}")
    public ResponseEntity<ClientEntity> getClientByRut(@PathVariable String rut){
        ClientEntity client = clientService.findByRut(rut);
        return ResponseEntity.ok(client);
    }
}
