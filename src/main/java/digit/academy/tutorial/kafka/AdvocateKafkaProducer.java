package digit.academy.tutorial.kafka;

import digit.academy.tutorial.web.models.Advocate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

// NOTE: If tracer is disabled change CustomKafkaTemplate to KafkaTemplate in autowiring

@Component
@RequiredArgsConstructor
public class AdvocateKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.advocate-create}")
    private String advocateCreateTopic;

    @Value("${kafka.topics.advocate-update}")
    private String advocateUpdateTopic;

    @Value("${kafka.topics.advocate-delete}")
    private String advocateDeleteTopic;

    public void sendAdvocateCreateEvent(Advocate advocate) {
        kafkaTemplate.send(advocateCreateTopic, advocate);
    }

    public void sendAdvocateUpdateEvent(Advocate advocate) {
        kafkaTemplate.send(advocateUpdateTopic, advocate);
    }

    public void sendAdvocateDeleteEvent(Advocate advocate) {
        kafkaTemplate.send(advocateDeleteTopic, advocate);
    }
}
