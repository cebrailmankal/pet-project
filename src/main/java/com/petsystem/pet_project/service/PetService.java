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

    // --- FOTOĞRAF KAYDETME (Türkçe Karakter Sorununu Çözen Yöntem) ---
    private String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        String uploadDir = "uploads";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Dosyanın orijinal uzantısını al (.jpg, .png gibi)
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Rastgele benzersiz bir isim oluştur (Türkçe karakter derdi kalmaz)
        String fileName = UUID.randomUUID().toString() + extension;
        String filePath = uploadDir + File.separator + fileName;

        file.transferTo(new File(filePath));
        return "/photos/" + fileName;
    }

    // --- PET İŞLEMLERİ ---
    public Pet addPet(String name, String species, int age, MultipartFile imageFile, String mail) throws IOException {
        User owner = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        if (owner.getRole() != Role.PET_OWNER) throw new RuntimeException("Sadece sahipler ekleyebilir");

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

    public void deletePet(Long petId, String mail) {
        User owner = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet bulunamadı"));
        if (!pet.getOwner().getId().equals(owner.getId())) throw new RuntimeException("Sadece kendi hayvanını silebilirsin");
        petRepository.delete(pet);
    }

    public List<Pet> getAvailablePets() { return petRepository.findByStatus("AVAILABLE"); }

    public List<Pet> getMyPets(String mail) {
        User owner = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return petRepository.findByOwner(owner);
    }

    // --- SAĞLIK KAYITLARI (VETERİNER) ---
    public HealthRecord addHealthRecord(Long petId, HealthRecord record, String mail) {
        User vet = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        if (vet.getRole() != Role.VET) throw new RuntimeException("Sadece Veteriner ekleyebilir");

        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));
        record.setPet(pet);
        record.setVetName(vet.getName());
        return healthRecordRepository.save(record);
    }

    public HealthRecord updateHealthRecord(Long recordId, HealthRecord newInfo, String mail) {
        User vet = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("User not found"));
        HealthRecord record = healthRecordRepository.findById(recordId).orElseThrow(() -> new RuntimeException("Kayıt bulunamadı"));

        if (vet.getRole() != Role.VET) throw new RuntimeException("Yetkiniz yok");

        record.setDiagnosis(newInfo.getDiagnosis());
        record.setTreatment(newInfo.getTreatment());
        record.setNotes(newInfo.getNotes());
        record.setDate(newInfo.getDate());
        return healthRecordRepository.save(record);
    }

    public void deleteHealthRecord(Long recordId, String mail) {
        User vet = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("User not found"));
        if (vet.getRole() != Role.VET) throw new RuntimeException("Yetkiniz yok");
        healthRecordRepository.deleteById(recordId);
    }

    // Veterinerin kendi eklediği tüm kayıtları getirir
    public List<HealthRecord> getRecordsByVet(String mail) {
        User vet = userRepository.findByEmail(mail).orElseThrow(() -> new RuntimeException("User not found"));
        return healthRecordRepository.findByVetName(vet.getName());
    }

    public List<HealthRecord> getHealthRecords(Long petId) {
        return healthRecordRepository.findByPetId(petId);
    }
}