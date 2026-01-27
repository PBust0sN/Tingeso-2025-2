package com.example.monolitico.Services;

import com.example.monolitico.Entities.ClientLoansEntity;
import com.example.monolitico.Repositories.ClientLoansRepository;
import com.example.monolitico.Service.ClientLoansService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientLoansServiceTest {

    @Mock
    private ClientLoansRepository clientLoansRepository;

    @InjectMocks
    private ClientLoansService clientLoansService;

    private ClientLoansEntity clientLoan;

    @BeforeEach
    void setUp() {
        clientLoan = new ClientLoansEntity(1L, 100L, 200L);
    }

    @Test
    void testGetAllClientsLoans() {
        when(clientLoansRepository.findAll()).thenReturn(List.of(clientLoan));

        List<ClientLoansEntity> result = clientLoansService.getAllClientsLoans();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(clientLoan, result.get(0));
        verify(clientLoansRepository, times(1)).findAll();
    }

    @Test
    void testSaveClientLoan() {
        when(clientLoansRepository.save(clientLoan)).thenReturn(clientLoan);

        ClientLoansEntity saved = clientLoansService.saveClientLoan(clientLoan);

        assertNotNull(saved);
        assertEquals(clientLoan.getIdClientLoans(), saved.getIdClientLoans());
        verify(clientLoansRepository, times(1)).save(clientLoan);
    }

    @Test
    void testGetClientLoansById() {
        when(clientLoansRepository.findById(1L)).thenReturn(Optional.of(clientLoan));

        ClientLoansEntity result = clientLoansService.getClientLoansById(1L);

        assertNotNull(result);
        assertEquals(clientLoan.getIdClientLoans(), result.getIdClientLoans());
        verify(clientLoansRepository, times(1)).findById(1L);
    }

    @Test
    void testGetClientLoansByIdNotFound() {
        when(clientLoansRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> clientLoansService.getClientLoansById(999L));
        verify(clientLoansRepository, times(1)).findById(999L);
    }

    @Test
    void testUpdateClientLoans() {
        when(clientLoansRepository.save(clientLoan)).thenReturn(clientLoan);

        ClientLoansEntity updated = clientLoansService.updateClientLoans(clientLoan);

        assertNotNull(updated);
        assertEquals(clientLoan.getIdClientLoans(), updated.getIdClientLoans());
        verify(clientLoansRepository, times(1)).save(clientLoan);
    }

    @Test
    void testDeleteClientLoans() throws Exception {
        doNothing().when(clientLoansRepository).deleteById(1L);

        boolean result = clientLoansService.deleteClientLoans(1L);

        assertTrue(result);
        verify(clientLoansRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteClientLoansThrowsException() {
        doThrow(new RuntimeException("DB error")).when(clientLoansRepository).deleteById(1L);

        Exception exception = assertThrows(Exception.class, () -> clientLoansService.deleteClientLoans(1L));

        assertEquals("DB error", exception.getMessage());
        verify(clientLoansRepository, times(1)).deleteById(1L);
    }
}
