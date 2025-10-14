package com.example.monolitico.Services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.monolitico.Entities.ClientBehindEntity;
import com.example.monolitico.Repositories.ClientBehindRepository;
import com.example.monolitico.Service.ClientBehindService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

class ClientBehindServiceTest {

    @Mock
    private ClientBehindRepository clientBehindRepository;

    @InjectMocks
    private ClientBehindService clientBehindService;

    private ClientBehindEntity clientBehind;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientBehind = new ClientBehindEntity();
        clientBehind.setClientIdBehind(1L);
        clientBehind.setReportId(100L);
        clientBehind.setRut("12345678-9");
        clientBehind.setName("John");
        clientBehind.setLastName("Doe");
        clientBehind.setMail("john.doe@example.com");
        clientBehind.setPhoneNumber("123456789");
        clientBehind.setState("active");
        clientBehind.setAvaliable(true);
    }

    @Test
    void testCreateClientBehind() {
        when(clientBehindRepository.save(any(ClientBehindEntity.class))).thenReturn(clientBehind);

        ClientBehindEntity result = clientBehindService.createClientBehind(clientBehind);

        assertNotNull(result);
        assertEquals(1L, result.getClientIdBehind());
        assertEquals("John", result.getName());
        verify(clientBehindRepository, times(1)).save(clientBehind);
    }

    @Test
    void testGetAllClientBehind() {
        when(clientBehindRepository.findAll()).thenReturn(Arrays.asList(clientBehind));

        List<ClientBehindEntity> result = clientBehindService.getAllClientBehind();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(clientBehind.getClientIdBehind(), result.get(0).getClientIdBehind());
        verify(clientBehindRepository, times(1)).findAll();
    }

    @Test
    void testGetClientBehindByReportId() {
        when(clientBehindRepository.findByReportId(100L)).thenReturn(clientBehind);

        ClientBehindEntity result = clientBehindService.getClientBehindByReportId(100L);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals(100L, result.getReportId());
        verify(clientBehindRepository, times(1)).findByReportId(100L);
    }

    @Test
    void testDeleteClientBehindById() throws Exception {
        doNothing().when(clientBehindRepository).deleteById(1L);

        boolean result = clientBehindService.deleteClientBehindById(1L);

        assertTrue(result);
        verify(clientBehindRepository, times(1)).deleteById(1L);
    }
}
