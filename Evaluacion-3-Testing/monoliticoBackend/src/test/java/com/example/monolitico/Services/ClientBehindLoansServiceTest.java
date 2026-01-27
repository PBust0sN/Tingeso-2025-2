package com.example.monolitico.Services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.monolitico.Entities.ClientBehindLoansEntity;
import com.example.monolitico.Repositories.ClientBehindLoansRepository;
import com.example.monolitico.Service.ClientBehindLoansService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

class ClientBehindLoansServiceTest {

    @Mock
    private ClientBehindLoansRepository clientBehindLoansRepository;

    @InjectMocks
    private ClientBehindLoansService clientBehindLoansService;

    private ClientBehindLoansEntity clientBehindLoans;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientBehindLoans = new ClientBehindLoansEntity();
        clientBehindLoans.setClientBehindLoansId(1L);
        clientBehindLoans.setLoanReportId(100L);
        clientBehindLoans.setClientIdBehind(200L);
    }

    @Test
    void testCreateCBL() {
        when(clientBehindLoansRepository.save(any(ClientBehindLoansEntity.class))).thenReturn(clientBehindLoans);

        ClientBehindLoansEntity result = clientBehindLoansService.createCBL(clientBehindLoans);

        assertNotNull(result);
        assertEquals(1L, result.getClientBehindLoansId());
        assertEquals(100L, result.getLoanReportId());
        assertEquals(200L, result.getClientIdBehind());
        verify(clientBehindLoansRepository, times(1)).save(clientBehindLoans);
    }

    @Test
    void testGetById() {
        when(clientBehindLoansRepository.getById(1L)).thenReturn(clientBehindLoans);

        ClientBehindLoansEntity result = clientBehindLoansService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getClientBehindLoansId());
        assertEquals(100L, result.getLoanReportId());
        assertEquals(200L, result.getClientIdBehind());
        verify(clientBehindLoansRepository, times(1)).getById(1L);
    }

    @Test
    void testFindClientIdByClientIdBehind() {
        List<Long> mockIds = Arrays.asList(10L, 20L, 30L);
        when(clientBehindLoansRepository.findLoansByClientIdBehind(200L)).thenReturn(mockIds);

        List<Long> result = clientBehindLoansService.findClientIdByClientIdBehind(200L);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(10L));
        assertTrue(result.contains(20L));
        assertTrue(result.contains(30L));
        verify(clientBehindLoansRepository, times(1)).findLoansByClientIdBehind(200L);
    }
}
