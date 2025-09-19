package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.ClientLoansEntity;
import com.example.monolitico.Entities.LoansEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientLoansRepository extends JpaRepository<ClientLoansEntity, Long> {

    public List<ClientLoansEntity> findByLoanId(Long loanId);

    @Query(value = "SELECT c.loan_id FROM client_loans c WHERE c.client_id = :clientId", nativeQuery = true)
    public List<Long> findLoansIdsByClientId(@Param("clientId") Long clientId);
}
