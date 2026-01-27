package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.LoansController;
import com.example.monolitico.DTO.NewLoanDTO;
import com.example.monolitico.DTO.ReturnLoanDTO;
import com.example.monolitico.Entities.LoansEntity;
import com.example.monolitico.Service.LoansService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoansControllerTest {

    @Mock
    private LoansService loansService;

    @InjectMocks
    private LoansController loansController;

    private LoansEntity loanExample;

    @BeforeEach
    void setUp() {
        loanExample = new LoansEntity(
                1L,
                new Date(System.currentTimeMillis()).toLocalDate(),
                new Date(System.currentTimeMillis() + 86400000).toLocalDate(), // +1 día
                "Normal",
                new Date(System.currentTimeMillis()).toLocalDate(),
                101L,
                202L,
                5000L,
                0L,
                true
        );
    }

    @Test
    void testGetAllLoans() {
        when(loansService.getAllLoans()).thenReturn(List.of(loanExample));

        ResponseEntity<List<LoansEntity>> response = loansController.getAllLoans();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(loansService, times(1)).getAllLoans();
    }

    @Test
    void testGetLoanById() {
        when(loansService.findLoanById(1L)).thenReturn(loanExample);

        ResponseEntity<LoansEntity> response = loansController.getLoanById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Normal", response.getBody().getLoanType());
        verify(loansService, times(1)).findLoanById(1L);
    }

    @Test
    void testSaveLoan() {
        when(loansService.saveLoan(any(LoansEntity.class))).thenReturn(Optional.of(loanExample));

        ResponseEntity<Optional<LoansEntity>> response = loansController.saveLoan(loanExample);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isPresent());
        verify(loansService, times(1)).saveLoan(loanExample);
    }

    @Test
    void testUpdateLoan() {
        when(loansService.updateLoan(any(LoansEntity.class))).thenReturn(loanExample);

        ResponseEntity<LoansEntity> response = loansController.updateLoan(loanExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(5000L, response.getBody().getAmount());
        verify(loansService, times(1)).updateLoan(loanExample);
    }

    @Test
    void testDeleteLoan() throws Exception {
        doReturn(true).when(loansService).deleteLoan(1L);

        ResponseEntity<LoansEntity> response = loansController.deleteLoan(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(loansService, times(1)).deleteLoan(1L);
    }

    @Test
    void testSaveNewLoan() {
        NewLoanDTO dto = new NewLoanDTO();
        dto.setStaff_id(101L);
        dto.setClient_id(202L);
        dto.setTools_id(List.of(10L, 11L));
        dto.setDays(5L);

        when(loansService.addLoan(101L, 202L, List.of(10L, 11L), 5L))
                .thenReturn(List.of("OK"));

        ResponseEntity<List<String>> response = loansController.saveNewLoan(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("OK", response.getBody().get(0));
        verify(loansService, times(1)).addLoan(101L, 202L, List.of(10L, 11L), 5L);
    }

    @Test
    void testReturnLoan() {
        // Creamos un ReturnLoanDTO con los campos existentes
        ReturnLoanDTO returnLoanDTO = new ReturnLoanDTO();
        returnLoanDTO.setLoan(loanExample);
        returnLoanDTO.setRepoAmount(1000L);
        returnLoanDTO.setFineAmount(200L);
        returnLoanDTO.setTools(List.of("Hammer", "Drill"));

        when(loansService.returnLoan(any(LoansEntity.class))).thenReturn(returnLoanDTO);

        ResponseEntity<ReturnLoanDTO> response = loansController.returnLoan(loanExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(loanExample, response.getBody().getLoan());
        assertEquals(1000L, response.getBody().getRepoAmount());
        assertEquals(200L, response.getBody().getFineAmount());
        assertEquals(List.of("Hammer", "Drill"), response.getBody().getTools());
        verify(loansService, times(1)).returnLoan(loanExample);
    }

    @Test
    void testCalculateLoanCost() {
        ReturnLoanDTO cost = new ReturnLoanDTO();
        cost.setLoan(loanExample);
        cost.setRepoAmount(5000L);
        cost.setFineAmount(500L);

        when(loansService.calculateCosts(1L)).thenReturn(cost);

        ResponseEntity<ReturnLoanDTO> response = loansController.calculateLoanCost(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(5000L, response.getBody().getRepoAmount());
        assertEquals(500L, response.getBody().getFineAmount());
        assertEquals(loanExample, response.getBody().getLoan());
        verify(loansService, times(1)).calculateCosts(1L);
    }

    @Test
    void testCheckDates() {
        when(loansService.checkDates(any(LoansEntity.class))).thenReturn(true);

        ResponseEntity<Boolean> response = loansController.checkDate(loanExample);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(loansService, times(1)).checkDates(loanExample);
    }
}
