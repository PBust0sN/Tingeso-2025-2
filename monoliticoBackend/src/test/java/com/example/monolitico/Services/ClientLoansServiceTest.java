package com.example.monolitico.Services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.monolitico.Entities.ClientLoansEntity;
import com.example.monolitico.Repositories.ClientLoansRepository;
import com.example.monolitico.Service.ClientLoansService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ClientLoansServiceTest {

    @Mock
    private ClientLoansRepository clientLoansRepository;

    @InjectMocks
    private ClientLoansService clientLoansService;

    private ClientLoansEntity clientLoans;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientLoans = new ClientLoansEntity();
        clientLoans.setIdClientLoans(1L);
        clientLoans.setClientId(10L);
        clientLoans.setLoanId(100L);
    }

    @Test
    void testSaveClientLoan() {
        when(clientLoansRepository.save(any(ClientLoansEntity.class))).thenReturn(clientLoans);

        ClientLoansEntity result = clientLoansService.saveClientLoan(clientLoans);

        assertNotNull(result);
        assertEquals(1L, result.getIdClientLoans());
        assertEquals(10L, result.getClientId());
        assertEquals(100L, result.getLoanId());
        verify(clientLoansRepository, times(1)).save(clientLoans);
    }

    @Test
    void testGetAllClientsLoans() {
        when(clientLoansRepository.findAll()).thenReturn(Arrays.asList(clientLoans));

        List<ClientLoansEntity> result = clientLoansService.getAllClientsLoans();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(clientLoans.getIdClientLoans(), result.get(0).getIdClientLoans());
        verify(clientLoansRepository, times(1)).findAll();
    }

    @Test
    void testGetClientLoansById() {
        when(clientLoansRepository.findById(1L)).thenReturn(Optional.of(clientLoans));

        ClientLoansEntity result = clientLoansService.getClientLoansById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdClientLoans());
        assertEquals(10L, result.getClientId());
        verify(clientLoansRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateClientLoans() {
        when(clientLoansRepository.save(any(ClientLoansEntity.class))).thenReturn(clientLoans);

        ClientLoansEntity result = clientLoansService.updateClientLoans(clientLoans);

        assertNotNull(result);
        assertEquals(clientLoans.getIdClientLoans(), result.getIdClientLoans());
        verify(clientLoansRepository, times(1)).save(clientLoans);
    }

    @Test
    void testDeleteClientLoans() throws Exception {
        doNothing().when(clientLoansRepository).deleteById(1L);

        boolean result = clientLoansService.deleteClientLoans(1L);

        assertTrue(result);
        verify(clientLoansRepository, times(1)).deleteById(1L);
    }
}
