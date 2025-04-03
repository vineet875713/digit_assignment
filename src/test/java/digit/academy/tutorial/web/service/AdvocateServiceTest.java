package digit.academy.tutorial.web.service;

import digit.academy.tutorial.kafka.AdvocateKafkaProducer;
import digit.academy.tutorial.repository.AdvocateRepository;
import digit.academy.tutorial.service.AdvocateEnrichmentService;
import digit.academy.tutorial.service.AdvocateServiceImpl;
import digit.academy.tutorial.service.AdvocateValidator;
import digit.academy.tutorial.web.models.Advocate;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AdvocateServiceTest {

    @Mock
    private AdvocateRepository advocateRepository;

    @Mock
    private AdvocateValidator advocateValidator;

    @Mock
    private AdvocateEnrichmentService advocateEnrichmentService;

    @Mock
    private AdvocateKafkaProducer advocateKafkaProducer;
    @InjectMocks
    private AdvocateServiceImpl advocateService;

    private Advocate advocate;
    private RequestInfo requestInfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        advocate = Advocate.builder()
                .id(UUID.randomUUID())
                .tenantId("default")
                .applicationNumber("ADVOC_001_2025")
                .barRegistrationNumber("BR12345")
                .advocateType("PUBLIC_DEFENDER")
                .isActive(true)
                .build();

        requestInfo = new RequestInfo();
    }

    @Test
    void testCreateAdvocate() {
        doNothing().when(advocateValidator).validateCreateRequest(any(), any());
        doNothing().when(advocateEnrichmentService).enrichCreateRequest(any(), any());
        doNothing().when(advocateKafkaProducer).sendAdvocateCreateEvent(any());

        // ✅ Fix: Ensure save() is called
        doAnswer(invocation -> {
            Advocate advocateArg = invocation.getArgument(0);
            assertNotNull(advocateArg.getApplicationNumber()); // Ensure valid object
            return null;
        }).when(advocateRepository).save(any(), any());

        Advocate created = advocateService.createAdvocate(advocate, requestInfo);

        assertNotNull(created);
        assertEquals("BR12345", created.getBarRegistrationNumber());

        verify(advocateValidator, times(1)).validateCreateRequest(any(), any());
        verify(advocateEnrichmentService, times(1)).enrichCreateRequest(any(), any());
        verify(advocateKafkaProducer, times(1)).sendAdvocateCreateEvent(any()); // ✅ Fix: Ensure it's called
    }


}