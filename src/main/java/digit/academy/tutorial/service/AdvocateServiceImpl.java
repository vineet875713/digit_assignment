package digit.academy.tutorial.service;

import digit.academy.tutorial.kafka.AdvocateKafkaProducer;
import digit.academy.tutorial.repository.AdvocateRepository;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class AdvocateServiceImpl implements AdvocateService {

    private final AdvocateRepository advocateRepository;
    private final AdvocateValidator advocateValidator;
    private final AdvocateEnrichmentService advocateEnrichmentService;
    private final RestTemplate restTemplate;
    private final AdvocateKafkaProducer advocateKafkaProducer;

    @Autowired
    public AdvocateServiceImpl(AdvocateRepository advocateRepository, AdvocateValidator advocateValidator,
                               AdvocateEnrichmentService advocateEnrichmentService, RestTemplate restTemplate, AdvocateKafkaProducer advocateKafkaProducer) {
        this.advocateRepository = advocateRepository;
        this.advocateValidator = advocateValidator;
        this.advocateEnrichmentService = advocateEnrichmentService;
        this.restTemplate = restTemplate;
        this.advocateKafkaProducer = advocateKafkaProducer;
    }

    @Override
    public Advocate createAdvocate(Advocate advocate, RequestInfo requestInfo) {
        advocateValidator.validateCreateRequest(advocate, requestInfo);
        advocateEnrichmentService.enrichCreateRequest(advocate, requestInfo);

        // Publish event to Kafka for Persister
        advocateKafkaProducer.sendAdvocateCreateEvent(advocate);

        return advocate;
    }


    @Override
    public Advocate updateAdvocate(Advocate advocate, RequestInfo requestInfo) {
        advocateValidator.validateUpdateRequest(advocate, requestInfo);
        advocateEnrichmentService.enrichUpdateRequest(advocate, requestInfo);

        // ✅ Send update event to Kafka (Persister will handle DB update)
        advocateKafkaProducer.sendAdvocateUpdateEvent(advocate);

        return advocate;
    }

    @Override
    public List<Advocate> searchAdvocates(AdvocateSearchCriteria criteria, RequestInfo requestInfo) {
        return advocateRepository.search(criteria);
    }

    @Override
    public List<Advocate> getPendingRegistrations() {
        return advocateRepository.getPendingApplications();
    }

    @Override
    public Advocate verifyAdvocate(String applicationNumber, String action, String rejectionReason, RequestInfo requestInfo) {
        Optional<Advocate> optionalAdvocate = advocateRepository.findByApplicationNumber(applicationNumber);
        if (optionalAdvocate.isEmpty()) {
            throw new IllegalArgumentException("Advocate not found");
        }

        Advocate advocate = optionalAdvocate.get();

        // Update workflow status based on action
        Workflow workflow = new Workflow();
        workflow.setAction(action);
        advocate.setWorkflow(workflow);

        // If rejected, store the reason
        if ("REJECT".equalsIgnoreCase(action)) {
            advocate.setRejectionReason(rejectionReason);
            sendSMS(advocate.getTenantId(), advocate.getApplicationNumber(), "Your advocate registration has been rejected.");
        } else {
            sendSMS(advocate.getTenantId(), advocate.getApplicationNumber(), "Your advocate registration has been approved.");
        }

        advocateRepository.update(advocate, requestInfo);
        return advocate;
    }

    private void sendSMS(String tenantId, String applicationNumber, String message) {
        Map<String, Object> smsRequest = new HashMap<>();
        smsRequest.put("tenantId", tenantId);
        smsRequest.put("applicationNumber", applicationNumber);
        smsRequest.put("message", message);

        restTemplate.postForObject("https://digit.org/sms/send", smsRequest, String.class);
    }


}
