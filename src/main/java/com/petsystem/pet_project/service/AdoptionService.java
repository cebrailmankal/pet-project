@Service
public class AdoptionService {

    private final AdoptionRequestRepository requestRepository;
    private final PetRepository petRepository;

    public AdoptionService(AdoptionRequestRepository requestRepository,
                           PetRepository petRepository) {
        this.requestRepository = requestRepository;
        this.petRepository = petRepository;
    }

    public AdoptionRequest createRequest(Long petId, User requester) {

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        if (pet.getStatus() != PetStatus.AVAILABLE) {
            throw new RuntimeException("Pet is not available for adoption");
        }

        if (pet.getOwner().getId().equals(requester.getId())) {
            throw new RuntimeException("You cannot adopt your own pet");
        }

        AdoptionRequest request = new AdoptionRequest();
        request.setPet(pet);
        request.setRequester(requester);
        request.setStatus(AdoptionRequest.Status.PENDING);

        return requestRepository.save(request);
    }

    public void approveRequest(Long requestId, User owner) {

        AdoptionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Pet pet = request.getPet();

        if (!pet.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Only pet owner can approve this request");
        }

        // Pet ownership transfer
        pet.setOwner(request.getRequester());
        pet.setStatus(PetStatus.ADOPTED);
        petRepository.save(pet);

        // Approve selected request
        request.setStatus(AdoptionRequest.Status.APPROVED);
        requestRepository.save(request);

        // Reject others
        List<AdoptionRequest> others =
                requestRepository.findByPetAndStatus(pet, AdoptionRequest.Status.PENDING);

        for (AdoptionRequest r : others) {
            r.setStatus(AdoptionRequest.Status.REJECTED);
            requestRepository.save(r);
        }
    }
}
