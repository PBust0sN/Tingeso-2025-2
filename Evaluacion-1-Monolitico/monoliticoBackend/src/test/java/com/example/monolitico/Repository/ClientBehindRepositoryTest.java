package com.example.monolitico.Repository;

import com.example.monolitico.Entities.ClientBehindEntity;
import com.example.monolitico.Repositories.ClientBehindRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ClientBehindRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientBehindRepository clientBehindRepository;

    @Test
    void testSaveAndFindById() {
        ClientBehindEntity client = new ClientBehindEntity();
        client.setReportId(100L);
        client.setRut("12.345.678-9");
        client.setName("Juan");
        client.setLastName("Pérez");
        client.setMail("juan.perez@mail.com");
        client.setPhoneNumber("987654321");
        client.setState("Activo");
        client.setAvaliable(true);

        // persistimos el cliente
        ClientBehindEntity persisted = entityManager.persistAndFlush(client);

        // recuperamos desde el repositorio
        Optional<ClientBehindEntity> found = clientBehindRepository.findById(persisted.getClientIdBehind());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Juan");
        assertThat(found.get().getRut()).isEqualTo("12.345.678-9");
    }

    @Test
    void testFindByReportId() {
        ClientBehindEntity client = new ClientBehindEntity();
        client.setReportId(200L);
        client.setRut("98.765.432-1");
        client.setName("María");
        client.setLastName("González");
        client.setMail("maria.gonzalez@mail.com");
        client.setPhoneNumber("123456789");
        client.setState("Inactivo");
        client.setAvaliable(false);

        entityManager.persistAndFlush(client);

        ClientBehindEntity found = clientBehindRepository.findByReportId(200L);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("María");
        assertThat(found.getAvaliable()).isFalse();
    }
}
