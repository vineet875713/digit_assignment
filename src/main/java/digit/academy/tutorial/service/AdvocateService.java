package digit.academy.tutorial.service;

import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdvocateService {

    // Creates a new advocate record
    Advocate createAdvocate(Advocate advocate, RequestInfo requestInfo);

    // Updates an existing advocate record
    Advocate updateAdvocate(Advocate advocate, RequestInfo requestInfo);

    // Search advocates using multiple criteria
    List<Advocate> searchAdvocates(AdvocateSearchCriteria criteria, RequestInfo requestInfo);

    // Fetch all pending registration requests for Nyay Mitra
    List<Advocate> getPendingRegistrations();

    // Verify, approve, or reject an advocate registration
    Advocate verifyAdvocate(String applicationNumber, String action, String rejectionReason, RequestInfo requestInfo);
}

