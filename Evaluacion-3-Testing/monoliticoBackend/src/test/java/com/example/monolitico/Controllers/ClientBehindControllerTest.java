package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.ClientBehindController;
import com.example.monolitico.Entities.ClientBehindEntity;
import com.example.monolitico.Service.ClientBehindService;
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
class ClientBehindControllerTest {

    @Mock
    private ClientBehindService clientBehindService;

    @InjectMocks
    private ClientBehindController clientBehindController;

    private ClientBehindEntity clientBehind;

    @BeforeEach
    void setUp() {
        clientBehind = new ClientBehindEntity();
        clientBehind.setClientIdBehind(1L);
        clientBehind.setReportId(10L);
        clientBehind.setRut("12.345.678-9");
        clientBehind.setName("Juan");
        clientBehind.setLastName("Pérez");
        clientBehind.setMail("juan.perez@example.com");
        clientBehind.setPhoneNumber("+56912345678");
        clientBehind.setState("ACTIVE");
        clientBehind.setAvaliable(true);
    }

    @Test
    void testGetClientBehindByReportId() {
        when(clientBehindService.getClientBehindByReportId(10L)).thenReturn(clientBehind);

        ResponseEntity<ClientBehindEntity> response = clientBehindController.getClientBehindByReportId(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Juan", response.getBody().getName());
        assertEquals("Pérez", response.getBody().getLastName());
        verify(clientBehindService, times(1)).getClientBehindByReportId(10L);
    }

    @Test
    void testGetAllClientBehind() {
        when(clientBehindService.getAllClientBehind()).thenReturn(List.of(clientBehind));

        ResponseEntity<List<ClientBehindEntity>> response = clientBehindController.getAllClientBehindByReportId();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Juan", response.getBody().get(0).getName());
        verify(clientBehindService, times(1)).getAllClientBehind();
    }

    @Test
    void testCreateClientBehind() {
        when(clientBehindService.createClientBehind(any(ClientBehindEntity.class))).thenReturn(clientBehind);

        ResponseEntity<ClientBehindEntity> response = clientBehindController.createClientBehind(clientBehind);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("juan.perez@example.com", response.getBody().getMail());
        verify(clientBehindService, times(1)).createClientBehind(any(ClientBehindEntity.class));
    }

    @Test
    void testDeleteClientBehind() throws Exception {
        when(clientBehindService.deleteClientBehindById(1L)).thenReturn(true);

        ResponseEntity<ClientBehindEntity> response = clientBehindController.deleteClientBehindById(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(clientBehindService, times(1)).deleteClientBehindById(1L);
    }
}
