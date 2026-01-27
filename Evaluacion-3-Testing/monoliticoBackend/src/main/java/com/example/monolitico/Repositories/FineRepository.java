package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.FineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<FineEntity, Long> {

    public List<FineEntity> getFineEntityByClientIdAndTypeIs(Long id, String type);
    public List<FineEntity> findByClientId(Long id);

    @Query(value = """
                        SELECT  * FROM fine WHERE state='pendiente' AND client_id=:id""",nativeQuery = true)
    public List<FineEntity> getPendingFinesByClientId(Long id);
}
