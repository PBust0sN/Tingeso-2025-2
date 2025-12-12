package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.LoansReportController;
import com.example.monolitico.Entities.LoansReportEntity;
import com.example.monolitico.Service.LoansReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoansReportControllerTest {

    @Mock
    private LoansReportService loansReportService;

    @InjectMocks
    private LoansReportController loansReportController;

    private LoansReportEntity reportExample;

    @BeforeEach
    void setUp() {
        reportExample = new LoansReportEntity(
                1L, // loanReportId
                100L, // reportId
                200L, // clientIdBehind
                new Date(System.currentTimeMillis()), // deliveryDate
                new Date(System.currentTimeMillis() + 86400000), // returnDate +1 día
                "Normal", // loanType
                new Date(System.currentTimeMillis()), // date
                10L, // staffId
                20L, // clientId
                5000L, // amount
                100L // extraCharges
        );
    }

    @Test
    void testGetAllLoansReport() {
        when(loansReportService.getAllLoansReport()).thenReturn(List.of(reportExample));

        ResponseEntity<List<LoansReportEntity>> response = loansReportController.getAllLoansReport();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(reportExample, response.getBody().get(0));
        verify(loansReportService, times(1)).getAllLoansReport();
    }

    @Test
    void testGetLoansReportByReportId() {
        when(loansReportService.getLoansReportByReportId(100L)).thenReturn(List.of(reportExample));

        ResponseEntity<List<LoansReportEntity>> response = loansReportController.getLoansReportByReportId(100L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(reportExample, response.getBody().get(0));
        verify(loansReportService, times(1)).getLoansReportByReportId(100L);
    }

    @Test
    void testGetLoansReportById() {
        when(loansReportService.getLoansReportById(1L)).thenReturn(reportExample);

        ResponseEntity<LoansReportEntity> response = loansReportController.getLoansReportById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(reportExample, response.getBody());
        verify(loansReportService, times(1)).getLoansReportById(1L);
    }

    @Test
    void testCreateLoansReport() {
        when(loansReportService.createLoansReport(reportExample)).thenReturn(reportExample);

        ResponseEntity<LoansReportEntity> response = loansReportController.createLoansReport(reportExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(reportExample, response.getBody());
        verify(loansReportService, times(1)).createLoansReport(reportExample);
    }

    @Test
    void testDeleteLoansReport() throws Exception {
        when(loansReportService.deleteLoansReportById(1L)).thenReturn(true);

        ResponseEntity<LoansReportEntity> response = loansReportController.deleteLoansReport(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(loansReportService, times(1)).deleteLoansReportById(1L);
    }
}
