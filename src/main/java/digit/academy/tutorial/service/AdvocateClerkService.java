package digit.academy.tutorial.service;

import digit.academy.tutorial.repository.AdvocateClerkRepository;
import digit.academy.tutorial.web.models.AdvocateClerk;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AdvocateClerkService {

    private final AdvocateClerkRepository advocateClerkRepository;

    @Autowired
    public AdvocateClerkService(AdvocateClerkRepository advocateClerkRepository) {
        this.advocateClerkRepository = advocateClerkRepository;
    }

    /**
     * Creates a new Advocate Clerk and associates it with an Advocate.
     */
    public AdvocateClerk createClerk(AdvocateClerk advocateClerk) {
        if (advocateClerk.getId() == null) {
            advocateClerk.setId(UUID.randomUUID());
        }
        advocateClerkRepository.save(advocateClerk);
        return advocateClerk;
    }
}
