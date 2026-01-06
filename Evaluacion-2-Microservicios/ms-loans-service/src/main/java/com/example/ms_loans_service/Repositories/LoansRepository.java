package com.example.ms_loans_service.Repositories;

import com.example.ms_loans_service.Entities.LoansEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LoansRepository extends JpaRepository<LoansEntity, Long> {

}
