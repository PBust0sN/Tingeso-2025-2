package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.ClientBehindLoansController;
import com.example.monolitico.Entities.ClientBehindLoansEntity;
import com.example.monolitico.Service.ClientBehindLoansService;
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
class ClientBehindLoansControllerTest {

    @Mock
    private ClientBehindLoansService clientBehindLoansService;

    @InjectMocks
    private ClientBehindLoansController clientBehindLoansController;

    private ClientBehindLoansEntity clientBehindLoansEntity;

    @BeforeEach
    void setUp() {
        clientBehindLoansEntity = new ClientBehindLoansEntity();
        clientBehindLoansEntity.setClientBehindLoansId(1L);
        clientBehindLoansEntity.setLoanReportId(20L);
        clientBehindLoansEntity.setClientIdBehind(5L);
    }

    @Test
    void testFindLoansIdByClientIdBehind() {
        when(clientBehindLoansService.findClientIdByClientIdBehind(5L)).thenReturn(List.of(101L, 102L, 103L));

        ResponseEntity<List<Long>> response = clientBehindLoansController.findLoansIdByClientIdBehind(5L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertTrue(response.getBody().contains(102L));

        verify(clientBehindLoansService, times(1)).findClientIdByClientIdBehind(5L);
    }

    @Test
    void testCreateCBL() {
        when(clientBehindLoansService.createCBL(any(ClientBehindLoansEntity.class)))
                .thenReturn(clientBehindLoansEntity);

        ResponseEntity<ClientBehindLoansEntity> response =
                clientBehindLoansController.createCBL(clientBehindLoansEntity);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(20L, response.getBody().getLoanReportId());
        assertEquals(5L, response.getBody().getClientIdBehind());

        verify(clientBehindLoansService, times(1)).createCBL(any(ClientBehindLoansEntity.class));
    }
}
