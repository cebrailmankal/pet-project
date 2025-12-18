package com.petsystem.pet_project.service;

import com.petsystem.pet_project.model.HealthRecord;
import com.petsystem.pet_project.model.Pet;
import com.petsystem.pet_project.model.Role;
import com.petsystem.pet_project.model.User;
import com.petsystem.pet_project.repository.HealthRecordRepository;
import com.petsystem.pet_project.repository.PetRepository;
import com.petsystem.pet_project.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final HealthRecordRepository healthRecordRepository;

    public PetService(PetRepository petRepository, UserRepository userRepository, HealthRecordRepository healthRecordRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.healthRecordRepository = healthRecordRepository;
    }

    // RESİM KAYDETME (Türkçe karakter ve çakışma önleyici)
    private String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        String uploadDir = "uploads";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        // Benzersiz isim oluştur
        String fileName = UUID.randomUUID().toString() + extension;
        String filePath = uploadDir + File.separator + fileName;

        file.transferTo(new File(filePath));
        return "/photos/" + fileName;
    }

    // PET EKLEME
    public Pet addPet(String name, String species, int age, MultipartFile imageFile, String mail) throws IOException {
        User owner = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("Kullanıcı yok"));
        if (owner.getRole() != Role.PET_OWNER) throw new RuntimeException("Yetkisiz");

        Pet pet = new Pet();
        pet.setName(name);
        pet.setSpecies(species);
        pet.setAge(age);
        pet.setOwner(owner);
        pet.setStatus("AVAILABLE");

        if (imageFile != null && !imageFile.isEmpty()) {
            pet.setImage(saveImage(imageFile));
        }
        return petRepository.save(pet);
    }

    // PET GÜNCELLEME (Edit)
    public Pet updatePet(Long petId, String name, String species, int age, MultipartFile imageFile, String mail) throws IOException {
        User owner = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("Kullanıcı yok"));
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet yok"));

        if (!pet.getOwner().getId().equals(owner.getId())) throw new RuntimeException("Sadece kendi hayvanını düzenleyebilirsin");

        pet.setName(name);
        pet.setSpecies(species);
        pet.setAge(age);

        if (imageFile != null && !imageFile.isEmpty()) {
            pet.setImage(saveImage(imageFile));
        }
        return petRepository.save(pet);
    }

    public void deletePet(Long petId, String mail) {
        User owner = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("Kullanıcı yok"));
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet yok"));
        if (!pet.getOwner().getId().equals(owner.getId())) throw new RuntimeException("Yetkisiz");
        petRepository.delete(pet);
    }

    public List<Pet> getAvailablePets() { return petRepository.findByStatus("AVAILABLE"); }
    public List<Pet> getMyPets(String mail) {
        User owner = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("Kullanıcı yok"));
        return petRepository.findByOwner(owner);
    }

    // --- VETERİNER İŞLEMLERİ ---
    public HealthRecord addHealthRecord(Long petId, HealthRecord record, String mail) {
        User vet = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("Kullanıcı yok"));
        if (vet.getRole() != Role.VET) throw new RuntimeException("Sadece veteriner ekleyebilir");

        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet bulunamadı"));
        record.setPet(pet);
        record.setVetName(vet.getName());
        return healthRecordRepository.save(record);
    }

    public HealthRecord updateHealthRecord(Long recordId, HealthRecord newInfo, String mail) {
        User vet = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("Kullanıcı yok"));
        if (vet.getRole() != Role.VET) throw new RuntimeException("Yetkisiz");

        HealthRecord record = healthRecordRepository.findById(recordId).orElseThrow(() -> new RuntimeException("Kayıt yok"));
        record.setDiagnosis(newInfo.getDiagnosis());
        record.setTreatment(newInfo.getTreatment());
        record.setNotes(newInfo.getNotes());
        record.setDate(newInfo.getDate());
        return healthRecordRepository.save(record);
    }

    public void deleteHealthRecord(Long recordId, String mail) {
        User vet = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("Kullanıcı yok"));
        if (vet.getRole() != Role.VET) throw new RuntimeException("Yetkisiz");
        healthRecordRepository.deleteById(recordId);
    }

    public List<HealthRecord> getRecordsByVet(String mail) {
        User vet = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("Kullanıcı yok"));
        return healthRecordRepository.findByVetName(vet.getName());
    }

    public List<HealthRecord> getHealthRecords(Long petId) {
        return healthRecordRepository.findByPetId(petId);
    }
}