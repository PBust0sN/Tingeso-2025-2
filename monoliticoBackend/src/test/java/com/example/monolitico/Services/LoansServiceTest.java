package com.example.monolitico.Services;

import com.example.monolitico.Entities.LoansEntity;
import com.example.monolitico.Repositories.LoansRepository;
import com.example.monolitico.Repositories.ToolsLoansRepository;
import com.example.monolitico.Service.LoansService;
import com.example.monolitico.Service.ToolsLoansService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoansServiceTest {

    @Mock
    private LoansRepository loansRepository;

    @Mock
    private ToolsLoansService toolsLoansService;

    @Mock
    private ToolsLoansRepository  toolsLoansRepository;

    @InjectMocks
    private LoansService loansService;

    private LoansEntity loan;

    @BeforeEach
    void setUp() {
        loan = new LoansEntity();
        loan.setLoanId(1L);
        loan.setDate(Date.valueOf(LocalDate.now()));
        loan.setDeliveryDate(Date.valueOf(LocalDate.now()));
        loan.setReturnDate(Date.valueOf(LocalDate.now().plusDays(5)));
        loan.setStaffId(1L);
        loan.setClientId(1L);
        loan.setAmount(100L);
        loan.setExtraCharges(0L);
        loan.setActive(true);
        loan.setLoanType("loan");
    }

    @Test
    void testSaveLoanWithReturnDate() {
        when(loansRepository.save(loan)).thenReturn(loan);

        Optional<LoansEntity> saved = loansService.saveLoan(loan);

        assertTrue(saved.isPresent());
        assertEquals(loan, saved.get());
        verify(loansRepository, times(1)).save(loan);
    }

    @Test
    void testSaveLoanWithoutReturnDate() {
        loan.setReturnDate(null);

        Optional<LoansEntity> saved = loansService.saveLoan(loan);

        assertTrue(saved.isEmpty());
        verify(loansRepository, never()).save(loan);
    }

    @Test
    void testCheckDatesValid() {
        assertTrue(loansService.checkDates(loan));
    }

    @Test
    void testCheckDatesInvalid() {
        loan.setReturnDate(Date.valueOf(LocalDate.now().minusDays(1)));
        assertFalse(loansService.checkDates(loan));
    }

    @Test
    void testFindLoanById() {
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));

        LoansEntity found = loansService.findLoanById(1L);

        assertEquals(loan, found);
        verify(loansRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateLoan() {
        when(loansRepository.save(loan)).thenReturn(loan);

        LoansEntity updated = loansService.updateLoan(loan);

        assertEquals(loan, updated);
        verify(loansRepository, times(1)).save(loan);
    }

    @Test
    void testDeleteLoanSuccess() throws Exception {
        Long loanId = 1L;

        // Mock del repositorio de loans
        doNothing().when(loansRepository).deleteById(loanId);

        // Mock de toolsLoansService para que devuelva algunas herramientas asociadas
        when(toolsLoansService.getToolsIDsByLoanId(loanId)).thenReturn(List.of(10L, 20L));

        // Mock del repositorio de toolsLoansRepository para que deleteByToolId no haga nada
        doNothing().when(toolsLoansRepository).deleteByToolId(anyLong());

        boolean result = loansService.deleteLoan(loanId);

        assertTrue(result);

        // Verifica que se llamó a todos los métodos correctamente
        verify(loansRepository, times(1)).deleteById(loanId);
        verify(toolsLoansService, times(1)).getToolsIDsByLoanId(loanId);
        verify(toolsLoansRepository, times(2)).deleteByToolId(anyLong());
    }
}
