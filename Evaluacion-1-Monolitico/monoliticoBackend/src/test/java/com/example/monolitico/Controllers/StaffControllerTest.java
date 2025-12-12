package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.StaffController;
import com.example.monolitico.Entities.StaffEntity;
import com.example.monolitico.Service.StaffService;
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
class StaffControllerTest {

    @Mock
    private StaffService staffService;

    @InjectMocks
    private StaffController staffController;

    private StaffEntity staffExample;

    @BeforeEach
    void setUp() {
        staffExample = new StaffEntity(
                1L, // staffId
                "12345678-9", // staffRut
                "Juan Perez", // staffName
                "juan.perez@mail.com", // staffMail
                "password123" // password
        );
    }

    @Test
    void testGetAllStaff() {
        when(staffService.getAllStaff()).thenReturn(List.of(staffExample));

        ResponseEntity<List<StaffEntity>> response = staffController.getAllStaff();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(staffExample, response.getBody().get(0));
        verify(staffService, times(1)).getAllStaff();
    }

    @Test
    void testGetStaffById() {
        when(staffService.getStaffById(1L)).thenReturn(staffExample);

        ResponseEntity<StaffEntity> response = staffController.getStaffById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(staffExample, response.getBody());
        verify(staffService, times(1)).getStaffById(1L);
    }

    @Test
    void testSaveStaff() {
        when(staffService.saveStaff(staffExample)).thenReturn(staffExample);

        ResponseEntity<StaffEntity> response = staffController.saveStaff(staffExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(staffExample, response.getBody());
        verify(staffService, times(1)).saveStaff(staffExample);
    }

    @Test
    void testUpdateStaff() {
        when(staffService.updateStaff(staffExample)).thenReturn(staffExample);

        ResponseEntity<StaffEntity> response = staffController.updateStaff(staffExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(staffExample, response.getBody());
        verify(staffService, times(1)).updateStaff(staffExample);
    }

    @Test
    void testDeleteStaff() throws Exception {
        when(staffService.deleteStaff(1L)).thenReturn(true);

        ResponseEntity<StaffEntity> response = staffController.deleteStaff(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(staffService, times(1)).deleteStaff(1L);
    }
}
