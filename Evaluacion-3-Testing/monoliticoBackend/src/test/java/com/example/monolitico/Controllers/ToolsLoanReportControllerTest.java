package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.ToolsLoanReportController;
import com.example.monolitico.Entities.ToolsLoanReportEntity;
import com.example.monolitico.Service.ToolsLoanReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolsLoanReportControllerTest {

    @Mock
    private ToolsLoanReportService toolsLoanReportService;

    @InjectMocks
    private ToolsLoanReportController toolsLoanReportController;

    private ToolsLoanReportEntity toolLoanReportExample;

    @BeforeEach
    void setUp() {
        toolLoanReportExample = new ToolsLoanReportEntity(
                1L, // toolLoanReportId
                101L, // loanId
                202L  // toolId
        );
    }

    @Test
    void testGetByLoanId() {
        when(toolsLoanReportService.findByLoanId(101L)).thenReturn(List.of(202L, 203L));

        ResponseEntity<List<Long>> response = toolsLoanReportController.getByLoanId(101L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().contains(202L));
        assertTrue(response.getBody().contains(203L));
        verify(toolsLoanReportService, times(1)).findByLoanId(101L);
    }

    @Test
    void testCreateToolsLoanReport() {
        when(toolsLoanReportService.createToolsLoanReport(toolLoanReportExample))
                .thenReturn(toolLoanReportExample);

        ResponseEntity<ToolsLoanReportEntity> response = toolsLoanReportController.create(toolLoanReportExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(toolLoanReportExample, response.getBody());
        verify(toolsLoanReportService, times(1)).createToolsLoanReport(toolLoanReportExample);
    }
}
