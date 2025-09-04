package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.FineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<FineEntity, Long> {

    public List<FineEntity> getFineEntityByClientIdAndTypeIs(Long id, String type);
}
