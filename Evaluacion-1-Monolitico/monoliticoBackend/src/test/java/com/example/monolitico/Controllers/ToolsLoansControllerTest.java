package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.ToolsLoansController;
import com.example.monolitico.Entities.ToolsLoansEntity;
import com.example.monolitico.Service.ToolsLoansService;
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
class ToolsLoansControllerTest {

    @Mock
    private ToolsLoansService toolsLoansService;

    @InjectMocks
    private ToolsLoansController toolsLoansController;

    private ToolsLoansEntity toolsLoansExample;

    @BeforeEach
    void setUp() {
        toolsLoansExample = new ToolsLoansEntity(
                1L, // id
                101L, // toolId
                202L  // loanId
        );
    }

    @Test
    void testGetAllToolsLoans() {
        when(toolsLoansService.getAllToolsLoans()).thenReturn(List.of(toolsLoansExample));

        ResponseEntity<List<ToolsLoansEntity>> response = toolsLoansController.getAllToolsLoans();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(toolsLoansExample, response.getBody().get(0));
        verify(toolsLoansService, times(1)).getAllToolsLoans();
    }

    @Test
    void testGetToolsLoansById() {
        when(toolsLoansService.getToolsLoansById(1L)).thenReturn(toolsLoansExample);

        ResponseEntity<ToolsLoansEntity> response = toolsLoansController.getToolsLoansById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(toolsLoansExample, response.getBody());
        verify(toolsLoansService, times(1)).getToolsLoansById(1L);
    }

    @Test
    void testSaveToolsLoans() {
        when(toolsLoansService.saveToolsLoans(toolsLoansExample)).thenReturn(toolsLoansExample);

        ResponseEntity<ToolsLoansEntity> response = toolsLoansController.saveToolsLoans(toolsLoansExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(toolsLoansExample, response.getBody());
        verify(toolsLoansService, times(1)).saveToolsLoans(toolsLoansExample);
    }

    @Test
    void testUpdateToolsLoans() {
        when(toolsLoansService.updateToolsLoans(toolsLoansExample)).thenReturn(toolsLoansExample);

        ResponseEntity<ToolsLoansEntity> response = toolsLoansController.updateToolsLoans(toolsLoansExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(toolsLoansExample, response.getBody());
        verify(toolsLoansService, times(1)).updateToolsLoans(toolsLoansExample);
    }

    @Test
    void testDeleteToolsLoans() throws Exception {
        when(toolsLoansService.deleteToolsLoans(1L)).thenReturn(true);

        ResponseEntity<ToolsLoansEntity> response = toolsLoansController.deleteToolsLoans(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(toolsLoansService, times(1)).deleteToolsLoans(1L);
    }

    @Test
    void testGetToolsIDsByLoanId() {
        when(toolsLoansService.getToolsIDsByLoanId(202L)).thenReturn(List.of(101L, 102L));

        ResponseEntity<List<Long>> response = toolsLoansController.getToolsIDsByLoanId(202L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().contains(101L));
        assertTrue(response.getBody().contains(102L));
        verify(toolsLoansService, times(1)).getToolsIDsByLoanId(202L);
    }
}
