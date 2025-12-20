package com.petsystem.pet_project.repository;

import com.petsystem.pet_project.model.HealthRecord;
import com.petsystem.pet_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

    // VIEW DETAILS
    // List<HealthRecord> findByPetIdOrderByDateDesc(Long petId);
    List<HealthRecord> findByPetId(Long petId);

    // VIEW RECORDS BY VET
    List<HealthRecord> findByVet(User vet);

    // UPDATE LATEST RECORDS
    Optional<HealthRecord> findFirstByPetIdOrderByDateDesc(Long petId);
}