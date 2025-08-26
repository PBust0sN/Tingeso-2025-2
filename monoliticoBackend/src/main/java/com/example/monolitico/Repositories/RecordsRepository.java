package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.RecordsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface RecordsRepository extends JpaRepository<RecordsEntity, Long> {
    public List<RecordsEntity> findByRecordDate(Date record_date);
    public List<RecordsEntity> findByRecordType(String record_type);
}
