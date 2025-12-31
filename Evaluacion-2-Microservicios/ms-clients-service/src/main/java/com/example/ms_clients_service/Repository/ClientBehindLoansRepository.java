package com.example.ms_clients_service.Repository;

import com.example.ms_clients_service.Entities.ClientBehindLoansEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientBehindLoansRepository extends JpaRepository<ClientBehindLoansEntity, Long> {
    @Query(value = "SELECT loan_report_id FROM client_behind_loans WHERE client_id_behind = :clientIdBehind", nativeQuery = true)
    public List<Long> findLoansByClientIdBehind(@Param("clientIdBehind") Long clientIdBehind);
}
