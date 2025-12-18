package com.petsystem.pet_project.repository;

import com.petsystem.pet_project.model.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
    List<HealthRecord> findByPetId(Long petId);
    List<HealthRecord> findByVetName(String vetName);
}