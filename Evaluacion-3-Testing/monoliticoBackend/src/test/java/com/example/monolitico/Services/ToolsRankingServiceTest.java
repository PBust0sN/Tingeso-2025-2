package com.example.monolitico.Services;

import com.example.monolitico.Entities.ToolsRankingEntity;
import com.example.monolitico.Repositories.ToolsRankingRepository;
import com.example.monolitico.Service.ToolsRankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolsRankingServiceTest {

    @Mock
    private ToolsRankingRepository toolsRankingRepository;

    @InjectMocks
    private ToolsRankingService toolsRankingService;

    private ToolsRankingEntity rankingEntity;

    @BeforeEach
    void setUp() {
        rankingEntity = new ToolsRankingEntity();
        rankingEntity.setToolRankingId(1L);
        rankingEntity.setReportId(100L);
        rankingEntity.setToolId(200L);
    }

    @Test
    void testGetAllToolsRanking() {
        when(toolsRankingRepository.findAll()).thenReturn(List.of(rankingEntity));

        List<ToolsRankingEntity> result = toolsRankingService.getAllToolsRanking();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(rankingEntity, result.get(0));
        verify(toolsRankingRepository, times(1)).findAll();
    }

    @Test
    void testGetToolsRankingByReportId() {
        when(toolsRankingRepository.findByReportId(100L)).thenReturn(List.of(rankingEntity));

        List<ToolsRankingEntity> result = toolsRankingService.getToolsRankingByReportId(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(rankingEntity, result.get(0));
        verify(toolsRankingRepository, times(1)).findByReportId(100L);
    }

    @Test
    void testCreateToolsRanking() {
        when(toolsRankingRepository.save(any(ToolsRankingEntity.class))).thenReturn(rankingEntity);

        ToolsRankingEntity saved = toolsRankingService.createToolsRanking(rankingEntity);

        assertNotNull(saved);
        assertEquals(rankingEntity.getToolRankingId(), saved.getToolRankingId());
        verify(toolsRankingRepository, times(1)).save(rankingEntity);
    }

    @Test
    void testDeleteToolsRankingById() throws Exception {
        doNothing().when(toolsRankingRepository).deleteById(1L);

        boolean result = toolsRankingService.deleteToolsRankingById(1L);

        assertTrue(result);
        verify(toolsRankingRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllToolsRankingEmpty() {
        when(toolsRankingRepository.findAll()).thenReturn(List.of());

        List<ToolsRankingEntity> result = toolsRankingService.getAllToolsRanking();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(toolsRankingRepository, times(1)).findAll();
    }

    @Test
    void testGetToolsRankingByReportIdEmpty() {
        when(toolsRankingRepository.findByReportId(999L)).thenReturn(List.of());

        List<ToolsRankingEntity> result = toolsRankingService.getToolsRankingByReportId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(toolsRankingRepository, times(1)).findByReportId(999L);
    }

    @Test
    void testDeleteToolsRankingByIdThrowsException() {
        doThrow(new RuntimeException("DB error")).when(toolsRankingRepository).deleteById(1L);

        Exception exception = assertThrows(Exception.class, () -> toolsRankingService.deleteToolsRankingById(1L));

        assertEquals("DB error", exception.getMessage());
        verify(toolsRankingRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCreateToolsRankingNull() {
        when(toolsRankingRepository.save(null)).thenThrow(new IllegalArgumentException("Entity is null"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> toolsRankingService.createToolsRanking(null));

        assertEquals("Entity is null", exception.getMessage());
        verify(toolsRankingRepository, times(1)).save(null);
    }

}
