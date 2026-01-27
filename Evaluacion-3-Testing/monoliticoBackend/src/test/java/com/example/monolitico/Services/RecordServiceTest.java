package com.example.monolitico.Services;

import com.example.monolitico.Entities.RecordsEntity;
import com.example.monolitico.Repositories.RecordsRepository;
import com.example.monolitico.Service.RecordsServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @InjectMocks
    private RecordsServices recordsServices;

    @Mock
    private RecordsRepository recordsRepository;

    private RecordsEntity record;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        record = new RecordsEntity();
        record.setRecordId(1L);
        record.setRecordType("Loan");
        record.setRecordAmount(100L);
        record.setClientId(1L);
        record.setLoanId(1L);
        record.setToolId(1L);
        record.setRecordDate(new Date(System.currentTimeMillis()));
    }

    @Test
    void testGetAllRecords() {
        List<RecordsEntity> recordsList = List.of(record);
        when(recordsRepository.findAll()).thenReturn(recordsList);

        List<RecordsEntity> result = recordsServices.getAllRecords();

        assertEquals(1, result.size());
        verify(recordsRepository, times(1)).findAll();
    }

    @Test
    void testSaveRecord() {
        when(recordsRepository.save(record)).thenReturn(record);

        RecordsEntity result = recordsServices.saveRecord(record);

        assertEquals(record.getRecordId(), result.getRecordId());
        verify(recordsRepository, times(1)).save(record);
    }

    @Test
    void testGetRecordsById() {
        when(recordsRepository.findById(1L)).thenReturn(Optional.of(record));

        RecordsEntity result = recordsServices.getRecordsById(1L);

        assertEquals(record.getRecordId(), result.getRecordId());
        verify(recordsRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateRecord() {
        when(recordsRepository.save(record)).thenReturn(record);

        RecordsEntity result = recordsServices.updateRecord(record);

        assertEquals(record.getRecordId(), result.getRecordId());
        verify(recordsRepository, times(1)).save(record);
    }

    @Test
    void testDeleteRecordSuccess() throws Exception {
        doNothing().when(recordsRepository).deleteById(1L);

        boolean result = recordsServices.deleteRecord(1L);

        assertTrue(result);
        verify(recordsRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteRecordThrowsException() {
        try {
            doThrow(new RuntimeException("Error")).when(recordsRepository).deleteById(1L);
            recordsServices.deleteRecord(1L);
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals("Error", e.getMessage());
        }
    }

    @Test
    void testFindByRecordDatesBetween() {
        Date startDate = Date.valueOf("2025-01-01");
        Date endDate = Date.valueOf("2025-12-31");
        List<RecordsEntity> recordsList = List.of(record);

        when(recordsRepository.findByRecordDateBetween(startDate, endDate)).thenReturn(recordsList);

        List<RecordsEntity> result = recordsServices.findByRecordDatesBetween(startDate, endDate);

        assertEquals(1, result.size());
        verify(recordsRepository, times(1)).findByRecordDateBetween(startDate, endDate);
    }
}
