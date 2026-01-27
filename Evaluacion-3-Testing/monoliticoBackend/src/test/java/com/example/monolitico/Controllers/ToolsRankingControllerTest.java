package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.ToolsRankingController;
import com.example.monolitico.Entities.ToolsRankingEntity;
import com.example.monolitico.Service.ToolsRankingService;
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
class ToolsRankingControllerTest {

    @Mock
    private ToolsRankingService toolsRankingService;

    @InjectMocks
    private ToolsRankingController toolsRankingController;

    private ToolsRankingEntity toolRankingExample;

    @BeforeEach
    void setUp() {
        toolRankingExample = new ToolsRankingEntity(
                1L, // toolRankingId
                101L, // reportId
                202L  // toolId
        );
    }

    @Test
    void testGetAllToolsRanking() {
        when(toolsRankingService.getAllToolsRanking()).thenReturn(List.of(toolRankingExample));

        ResponseEntity<List<ToolsRankingEntity>> response = toolsRankingController.getAllToolsRanking();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(toolRankingExample, response.getBody().get(0));
        verify(toolsRankingService, times(1)).getAllToolsRanking();
    }

    @Test
    void testGetToolsRankingById() {
        when(toolsRankingService.getToolsRankingByReportId(101L)).thenReturn(List.of(toolRankingExample));

        ResponseEntity<List<ToolsRankingEntity>> response = toolsRankingController.getToolsRankingById(101L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(toolRankingExample, response.getBody().get(0));
        verify(toolsRankingService, times(1)).getToolsRankingByReportId(101L);
    }

    @Test
    void testCreateToolsRanking() {
        when(toolsRankingService.createToolsRanking(toolRankingExample)).thenReturn(toolRankingExample);

        ResponseEntity<ToolsRankingEntity> response = toolsRankingController.createToolsRanking(toolRankingExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(toolRankingExample, response.getBody());
        verify(toolsRankingService, times(1)).createToolsRanking(toolRankingExample);
    }

    @Test
    void testDeleteToolsRanking() throws Exception {
        when(toolsRankingService.deleteToolsRankingById(1L)).thenReturn(true);

        ResponseEntity<ToolsRankingEntity> response = toolsRankingController.deleteToolsRanking(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(toolsRankingService, times(1)).deleteToolsRankingById(1L);
    }
}
