package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.ToolsController;
import com.example.monolitico.Entities.ToolsEntity;
import com.example.monolitico.Service.ToolsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolsControllerTest {

    @Mock
    private ToolsService toolsService;

    @InjectMocks
    private ToolsController toolsController;

    private ToolsEntity toolExample;

    @BeforeEach
    void setUp() {
        toolExample = new ToolsEntity(
                1L, "Taladro", "Bueno", "Disponible", "Eléctrico",
                1000L, 500L, 50L, 10L, 5L, 20L
        );
    }

    @Test
    void testGetAllTools() {
        when(toolsService.getAllTools()).thenReturn(List.of(toolExample));

        ResponseEntity<List<ToolsEntity>> response = toolsController.getAllTools();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(toolExample, response.getBody().get(0));
        verify(toolsService, times(1)).getAllTools();
    }

    @Test
    void testGetToolById() {
        when(toolsService.getToolsById(1L)).thenReturn(toolExample);

        ResponseEntity<ToolsEntity> response = toolsController.getToolById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(toolExample, response.getBody());
        verify(toolsService, times(1)).getToolsById(1L);
    }

    @Test
    void testSaveTool() {
        when(toolsService.saveTool(toolExample)).thenReturn(Optional.of(toolExample));

        ResponseEntity<Optional<ToolsEntity>> response = toolsController.saveTool(toolExample);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isPresent());
        assertEquals(toolExample, response.getBody().get());
        verify(toolsService, times(1)).saveTool(toolExample);
    }

    @Test
    void testUpdateTool() {
        when(toolsService.updateTool(toolExample)).thenReturn(toolExample);

        ResponseEntity<ToolsEntity> response = toolsController.updateTool(toolExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(toolExample, response.getBody());
        verify(toolsService, times(1)).updateTool(toolExample);
    }

    @Test
    void testDropdownTool() {
        when(toolsService.dropDownATool(1L, 1L)).thenReturn(Optional.of(toolExample));

        ResponseEntity<Optional<ToolsEntity>> response = toolsController.dropdownTool(1L, 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isPresent());
        assertEquals(toolExample, response.getBody().get());
        verify(toolsService, times(1)).dropDownATool(1L, 1L);
    }

    @Test
    void testDeleteTool() throws Exception {
        when(toolsService.deleteTools(1L)).thenReturn(true);

        ResponseEntity<ToolsEntity> response = toolsController.deleteTool(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(toolsService, times(1)).deleteTools(1L);
    }

    @Test
    void testUpdateToolState() {
        when(toolsService.updateStateById(1L, "Malo")).thenReturn(toolExample);

        ResponseEntity<ToolsEntity> response = toolsController.updateToolState(1L, "Malo");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(toolExample, response.getBody());
        verify(toolsService, times(1)).updateStateById(1L, "Malo");
    }

    @Test
    void testUpdateToolStock() {
        when(toolsService.updateStockById(1L)).thenReturn(toolExample);

        ResponseEntity<ToolsEntity> response = toolsController.updateToolStock(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(toolExample, response.getBody());
        verify(toolsService, times(1)).updateStockById(1L);
    }

    @Test
    void testGetTopTenToolsLoanCount() {
        when(toolsService.findRankingMax10()).thenReturn(List.of(toolExample));

        ResponseEntity<List<ToolsEntity>> response = toolsController.getTopTenToolsLoanCount();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(toolExample, response.getBody().get(0));
        verify(toolsService, times(1)).findRankingMax10();
    }
}

