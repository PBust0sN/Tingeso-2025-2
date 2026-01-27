package com.example.monolitico.Repository;

import com.example.monolitico.Entities.ClientLoansEntity;
import com.example.monolitico.Repositories.ClientLoansRepository;
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
class ClientLoansRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientLoansRepository clientLoansRepository;

    @Test
    void testSaveAndFindById() {
        ClientLoansEntity relation = new ClientLoansEntity();
        relation.setClientId(1L);
        relation.setLoanId(10L);

        ClientLoansEntity saved = entityManager.persistAndFlush(relation);

        assertThat(saved.getIdClientLoans()).isNotNull();

        ClientLoansEntity found = clientLoansRepository.findById(saved.getIdClientLoans()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getClientId()).isEqualTo(1L);
        assertThat(found.getLoanId()).isEqualTo(10L);
    }

    @Test
    void testFindByLoanId() {
        ClientLoansEntity relation1 = new ClientLoansEntity(null, 2L, 20L);
        ClientLoansEntity relation2 = new ClientLoansEntity(null, 3L, 20L);
        ClientLoansEntity relation3 = new ClientLoansEntity(null, 4L, 21L);

        entityManager.persist(relation1);
        entityManager.persist(relation2);
        entityManager.persist(relation3);
        entityManager.flush();

        List<ClientLoansEntity> results = clientLoansRepository.findByLoanId(20L);

        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(ClientLoansEntity::getClientId)
                .containsExactlyInAnyOrder(2L, 3L);
    }

    @Test
    void testFindLoansIdsByClientId() {
        ClientLoansEntity relation1 = new ClientLoansEntity(null, 5L, 30L);
        ClientLoansEntity relation2 = new ClientLoansEntity(null, 5L, 31L);
        ClientLoansEntity relation3 = new ClientLoansEntity(null, 6L, 32L);

        entityManager.persist(relation1);
        entityManager.persist(relation2);
        entityManager.persist(relation3);
        entityManager.flush();

        List<Long> loanIds = clientLoansRepository.findLoansIdsByClientId(5L);

        assertThat(loanIds).hasSize(2);
        assertThat(loanIds).containsExactlyInAnyOrder(30L, 31L);
    }
}
