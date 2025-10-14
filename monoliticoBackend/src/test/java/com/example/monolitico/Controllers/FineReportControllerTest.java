package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.FineReportController;
import com.example.monolitico.Entities.FineReportEntity;
import com.example.monolitico.Service.FineReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FineReportControllerTest {

    @Mock
    private FineReportService fineReportService;

    @InjectMocks
    private FineReportController fineReportController;

    private FineReportEntity fineReportExample;

    @BeforeEach
    void setUp() {
        fineReportExample = new FineReportEntity(
                1L,
                10L,
                15000L,
                "Payment Delay",
                3L,
                7L,
                "Pending",
                new Date(System.currentTimeMillis())
        );
    }

    @Test
    void testGetFineReportByReportId() {
        when(fineReportService.getFineReportsByReportId(10L)).thenReturn(List.of(fineReportExample));

        ResponseEntity<List<FineReportEntity>> response = fineReportController.getFineReportByReportId(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Payment Delay", response.getBody().get(0).getType());
        verify(fineReportService, times(1)).getFineReportsByReportId(10L);
    }

    @Test
    void testGetAllFineReports() {
        when(fineReportService.getAllFineReport()).thenReturn(Arrays.asList(fineReportExample));

        ResponseEntity<List<FineReportEntity>> response = fineReportController.getAllFineReports();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Pending", response.getBody().get(0).getState());
        verify(fineReportService, times(1)).getAllFineReport();
    }

    @Test
    void testCreateFineReport() {
        when(fineReportService.createFineReport(any(FineReportEntity.class))).thenReturn(fineReportExample);

        ResponseEntity<FineReportEntity> response = fineReportController.createFineReport(fineReportExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(15000L, response.getBody().getAmount());
        verify(fineReportService, times(1)).createFineReport(fineReportExample);
    }

    @Test
    void testDeleteFineReport() throws Exception {
        doReturn(true).when(fineReportService).deleteFineReportById(1L);

        ResponseEntity<FineReportEntity> response = fineReportController.deleteFineReport(1L);

        assertEquals(204, response.getStatusCodeValue()); // No Content
        verify(fineReportService, times(1)).deleteFineReportById(1L);
    }
}
