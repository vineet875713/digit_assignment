package digit.academy.tutorial.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Repository
@Slf4j
public class ServiceRequestRepository {

    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;

    @Autowired
    public ServiceRequestRepository(ObjectMapper mapper, RestTemplate restTemplate) {
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }

    public <T> T fetchResult(StringBuilder uri, Object request, Class<T> responseType) {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            log.info("Calling external service: {}", uri);
            return restTemplate.postForObject(uri.toString(), request, responseType);
        } catch (HttpClientErrorException e) {
            log.error("External service call failed: {}", e.getResponseBodyAsString(), e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Error while calling external service", e);
            throw new RuntimeException("Service call failed", e);
        }
    }
}
