package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.ToolsReportController;
import com.example.monolitico.Entities.ToolsReportEntity;
import com.example.monolitico.Service.ToolsReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolsReportControllerTest {

    @Mock
    private ToolsReportService toolsReportService;

    @InjectMocks
    private ToolsReportController toolsReportController;

    private ToolsReportEntity toolReportExample;

    @BeforeEach
    void setUp() {
        toolReportExample = new ToolsReportEntity(
                1L,           // toolIdReport
                "Hammer",     // toolName
                "Hand Tool",  // category
                15L           // loanCount
        );
    }

    @Test
    void testGetToolsReportById() {
        when(toolsReportService.getByToolId(1L)).thenReturn(toolReportExample);

        ResponseEntity<ToolsReportEntity> response = toolsReportController.getToolsReportById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(toolReportExample, response.getBody());
        verify(toolsReportService, times(1)).getByToolId(1L);
    }

    @Test
    void testSaveToolsReport() {
        when(toolsReportService.createToolReport(toolReportExample)).thenReturn(toolReportExample);

        ResponseEntity<ToolsReportEntity> response = toolsReportController.saveToolsReport(toolReportExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(toolReportExample, response.getBody());
        verify(toolsReportService, times(1)).createToolReport(toolReportExample);
    }
}
