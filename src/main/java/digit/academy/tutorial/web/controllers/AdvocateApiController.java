package digit.academy.tutorial.web.controllers;


import digit.academy.tutorial.service.AdvocateClerkService;
import digit.academy.tutorial.service.AdvocateService;
import digit.academy.tutorial.util.ResponseInfoFactory;
import digit.academy.tutorial.web.models.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.RequestInfoWrapper;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-02-10T11:17:28.221361812+05:30[Asia/Kolkata]")
@RestController
@RequestMapping("/advocate/v1")
@Slf4j
public class AdvocateApiController {

    private final AdvocateService advocateService;
    private final ResponseInfoFactory responseInfoFactory;
    private final AdvocateClerkService advocateClerkService;

    @Autowired
    public AdvocateApiController(AdvocateService advocateService, ResponseInfoFactory responseInfoFactory, AdvocateClerkService advocateClerkService) {
        this.advocateService = advocateService;
        this.responseInfoFactory = responseInfoFactory;
        this.advocateClerkService = advocateClerkService;
    }

    /**
     * Create Advocate
     */
    @PostMapping("/_create")
    public ResponseEntity<AdvocateResponse> createAdvocate(@Valid @RequestBody AdvocateRequest body) {
        log.info("Received Advocate Create request: {}", body);
        List<Advocate> createdAdvocates = new ArrayList<>();

        for (Advocate advocate : body.getAdvocates()) {
            createdAdvocates.add(advocateService.createAdvocate(advocate, body.getRequestInfo()));
        }

        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        return ResponseEntity.ok(new AdvocateResponse(responseInfo, createdAdvocates, null));
    }

    /**
     * Search Advocates
     */
    @PostMapping("/_search")
    public ResponseEntity<AdvocateResponse> searchAdvocates(@Valid @RequestBody AdvocateSearchRequest body) {
        log.info("Received Advocate Search request: {}", body);

        List<Advocate> foundAdvocates = advocateService.searchAdvocates(body.getCriteria().get(0), body.getRequestInfo());

        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        return ResponseEntity.ok(new AdvocateResponse(responseInfo, foundAdvocates, null));
    }

    /**
     * Update Advocate
     */
    @PostMapping("/_update")
    public ResponseEntity<AdvocateResponse> updateAdvocate(@Valid @RequestBody AdvocateRequest body) {
        log.info("Received Advocate Update request: {}", body);
        List<Advocate> updatedAdvocates = new ArrayList<>();

        for (Advocate advocate : body.getAdvocates()) {
            updatedAdvocates.add(advocateService.updateAdvocate(advocate, body.getRequestInfo()));
        }

        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        return ResponseEntity.ok(new AdvocateResponse(responseInfo, updatedAdvocates, null));
    }

    @PostMapping("/_pendingRegistrations")
    public ResponseEntity<AdvocateResponse> getPendingRegistrations(@Valid @RequestBody RequestInfoWrapper request) {
        log.info("Fetching pending registrations for Nyay Mitra");

        List<Advocate> pendingAdvocates = advocateService.getPendingRegistrations();

        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
        return ResponseEntity.ok(new AdvocateResponse(responseInfo, pendingAdvocates, null));
    }


    @PostMapping("/_verify")
    public ResponseEntity<AdvocateResponse> verifyRegistration(@Valid @RequestBody AdvocateVerificationRequest body) {
        log.info("Received verification request: {}", body);

        Advocate advocate = advocateService.verifyAdvocate(body.getApplicationNumber(), body.getAction(), body.getRejectionReason(), body.getRequestInfo());

        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        return ResponseEntity.ok(new AdvocateResponse(responseInfo, List.of(advocate), null));
    }

    @PostMapping("/clerk/_create")
    public ResponseEntity<AdvocateClerkResponse> createClerk(@Valid @RequestBody AdvocateClerkRequest body) {
        log.info("Received Clerk Registration request: {}", body);

        // Create the clerk using the service
        AdvocateClerk clerk = advocateClerkService.createClerk(body.getAdvocateClerk());

        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        return ResponseEntity.ok(new AdvocateClerkResponse(responseInfo, List.of(clerk)));
    }

}

