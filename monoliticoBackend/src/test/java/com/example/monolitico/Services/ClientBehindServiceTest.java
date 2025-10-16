package com.example.monolitico.Services;

import com.example.monolitico.Entities.ClientBehindEntity;
import com.example.monolitico.Repositories.ClientBehindRepository;
import com.example.monolitico.Service.ClientBehindService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        clientBehind.setName("Juan");
        clientBehind.setLastName("Perez");
        clientBehind.setMail("juan@example.com");
        clientBehind.setPhoneNumber("987654321");
        clientBehind.setState("active");
        clientBehind.setAvaliable(true);
    }

    @Test
    void testGetAllClientBehind() {
        when(clientBehindRepository.findAll()).thenReturn(List.of(clientBehind));

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
        assertEquals(100L, result.getReportId());
        assertEquals("Juan", result.getName());

        verify(clientBehindRepository, times(1)).findByReportId(100L);
    }

    @Test
    void testCreateClientBehind() {
        when(clientBehindRepository.save(any(ClientBehindEntity.class))).thenReturn(clientBehind);

        ClientBehindEntity result = clientBehindService.createClientBehind(clientBehind);

        assertNotNull(result);
        assertEquals(clientBehind.getClientIdBehind(), result.getClientIdBehind());
        verify(clientBehindRepository, times(1)).save(clientBehind);
    }

    @Test
    void testDeleteClientBehindById_Success() throws Exception {
        doNothing().when(clientBehindRepository).deleteById(1L);

        boolean result = clientBehindService.deleteClientBehindById(1L);

        assertTrue(result);
        verify(clientBehindRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteClientBehindById_Exception() {
        doThrow(new RuntimeException("DB Error")).when(clientBehindRepository).deleteById(1L);

        Exception exception = assertThrows(Exception.class, () -> clientBehindService.deleteClientBehindById(1L));
        assertTrue(exception.getMessage().contains("DB Error"));

        verify(clientBehindRepository, times(1)).deleteById(1L);
    }
}
