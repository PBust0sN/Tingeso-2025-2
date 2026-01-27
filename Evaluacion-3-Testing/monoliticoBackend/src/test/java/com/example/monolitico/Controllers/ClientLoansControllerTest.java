package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.ClientLoansController;
import com.example.monolitico.Entities.ClientLoansEntity;
import com.example.monolitico.Service.ClientLoansService;
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
class ClientLoansControllerTest {

    @Mock
    private ClientLoansService clientLoansService;

    @InjectMocks
    private ClientLoansController clientLoansController;

    private ClientLoansEntity clientLoansEntity;

    @BeforeEach
    void setUp() {
        clientLoansEntity = new ClientLoansEntity();
        clientLoansEntity.setIdClientLoans(1L);
        clientLoansEntity.setClientId(100L);
        clientLoansEntity.setLoanId(200L);
    }

    @Test
    void testGetAllClientsLoans() {
        when(clientLoansService.getAllClientsLoans()).thenReturn(List.of(clientLoansEntity));

        ResponseEntity<List<ClientLoansEntity>> response = clientLoansController.getAllClients();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(100L, response.getBody().get(0).getClientId());

        verify(clientLoansService, times(1)).getAllClientsLoans();
    }

    @Test
    void testGetClientLoansById() {
        when(clientLoansService.getClientLoansById(1L)).thenReturn(clientLoansEntity);

        ResponseEntity<ClientLoansEntity> response = clientLoansController.getClientById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(200L, response.getBody().getLoanId());

        verify(clientLoansService, times(1)).getClientLoansById(1L);
    }

    @Test
    void testSaveClientLoan() {
        when(clientLoansService.saveClientLoan(any(ClientLoansEntity.class))).thenReturn(clientLoansEntity);

        ResponseEntity<ClientLoansEntity> response = clientLoansController.saveClient(clientLoansEntity);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(100L, response.getBody().getClientId());
        verify(clientLoansService, times(1)).saveClientLoan(any(ClientLoansEntity.class));
    }

    @Test
    void testUpdateClientLoans() {
        clientLoansEntity.setLoanId(300L);
        when(clientLoansService.updateClientLoans(any(ClientLoansEntity.class))).thenReturn(clientLoansEntity);

        ResponseEntity<ClientLoansEntity> response = clientLoansController.updateClient(clientLoansEntity);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(300L, response.getBody().getLoanId());
        verify(clientLoansService, times(1)).updateClientLoans(any(ClientLoansEntity.class));
    }

    @Test
    void testDeleteClientLoans() throws Exception {
        when(clientLoansService.deleteClientLoans(1L)).thenReturn(true);

        ResponseEntity<ClientLoansEntity> response = clientLoansController.deleteClient(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(clientLoansService, times(1)).deleteClientLoans(1L);
    }
}
