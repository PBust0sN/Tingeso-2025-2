package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.ClientController;
import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private ClientEntity clientEntity;

    @BeforeEach
    void setUp() {
        clientEntity = new ClientEntity();
        clientEntity.setClient_id(1L);
        clientEntity.setRut("12.345.678-9");
        clientEntity.setName("Juan");
        clientEntity.setLast_name("Pérez");
        clientEntity.setMail("juan@example.com");
        clientEntity.setPhone_number("999999999");
        clientEntity.setState("Activo");
        clientEntity.setAvaliable(true);
        clientEntity.setPassword("1234");
    }

    @Test
    void testGetAllClients() {
        when(clientService.getAllClients()).thenReturn(List.of(clientEntity));

        ResponseEntity<List<ClientEntity>> response = clientController.getAllClients();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Juan", response.getBody().get(0).getName());

        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void testGetClientById() {
        when(clientService.getClientById(1L)).thenReturn(clientEntity);

        ResponseEntity<ClientEntity> response = clientController.getClientById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Juan", response.getBody().getName());

        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void testSaveClient() {
        when(clientService.saveClient(any(ClientEntity.class))).thenReturn(clientEntity);

        ResponseEntity<ClientEntity> response = clientController.saveClient(clientEntity);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("juan@example.com", response.getBody().getMail());
        verify(clientService, times(1)).saveClient(any(ClientEntity.class));
    }

    @Test
    void testUpdateClient() {
        clientEntity.setName("Pedro");
        when(clientService.updateClient(any(ClientEntity.class))).thenReturn(clientEntity);

        ResponseEntity<ClientEntity> response = clientController.updateClient(clientEntity);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Pedro", response.getBody().getName());
        verify(clientService, times(1)).updateClient(any(ClientEntity.class));
    }

    @Test
    void testDeleteClient() throws Exception {
        when(clientService.deleteClient(1L)).thenReturn(true);

        ResponseEntity<ClientEntity> response = clientController.deleteClient(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(clientService, times(1)).deleteClient(1L);
    }

    @Test
    void testGetClientByRut() {
        when(clientService.findByRut("12.345.678-9")).thenReturn(clientEntity);

        ResponseEntity<ClientEntity> response = clientController.getClientByRut("12.345.678-9");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Juan", response.getBody().getName());
        verify(clientService, times(1)).findByRut("12.345.678-9");
    }
}
