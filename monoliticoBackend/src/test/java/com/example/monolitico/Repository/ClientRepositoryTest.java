package com.example.monolitico.Repository;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.LoansEntity;
import com.example.monolitico.Repositories.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ClientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void testSaveAndFindById() {
        ClientEntity client = new ClientEntity();
        client.setRut("12.345.678-9");
        client.setName("Juan");
        client.setLast_name("Pérez");
        client.setMail("juan@example.com");
        client.setPhone_number("123456789");
        client.setState("activo");
        client.setAvaliable(true);
        client.setPassword("1234");

        ClientEntity saved = entityManager.persistAndFlush(client);

        assertThat(saved.getClient_id()).isNotNull();

        ClientEntity found = clientRepository.findById(saved.getClient_id()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getRut()).isEqualTo("12.345.678-9");
    }

    @Test
    void testFindByRut() {
        ClientEntity client = new ClientEntity(null, "11.111.111-1", "Ana", "Torres", "ana@mail.com", "555", "activo", true, "pass");
        entityManager.persistAndFlush(client);

        ClientEntity found = clientRepository.findByRut("11.111.111-1");

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Ana");
    }

    @Test
    void testFindByMail() {
        ClientEntity client1 = new ClientEntity(null, "22.222.222-2", "Pedro", "Gómez", "pedro@mail.com", "111", "activo", true, "pass1");
        ClientEntity client2 = new ClientEntity(null, "33.333.333-3", "Lucía", "Martínez", "lucia@mail.com", "222", "activo", true, "pass2");

        entityManager.persist(client1);
        entityManager.persist(client2);
        entityManager.flush();

        List<ClientEntity> found = clientRepository.findByMail("pedro@mail.com");

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Pedro");
    }

    @Test
    void testFindByRutNotFound() {
        ClientEntity found = clientRepository.findByRut("99.999.999-9");
        assertThat(found).isNull();
    }

}
