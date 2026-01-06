package com.example.ms_clients_service.Repository;

import com.example.ms_clients_service.Entities.ClientBehindEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientBehindRepository extends JpaRepository<ClientBehindEntity, Long> {
    public ClientBehindEntity findByReportId(Long  reportId);
}
