package aplication.pubsub.publish;

import aplication.config.pubsub.PubSubProperties;
import domain.entities.PayloadMessageExample;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ExamplePublish {

    private final PubSubTemplate pubSubTemplate;
    private final PubSubProperties pubSubProperties;

    @PostConstruct
    public void publishExample() {
        PayloadMessageExample payload = new PayloadMessageExample("Hello World", 10L);

        var topic = pubSubProperties.getTopics().get("exampleTopic");

        log.info("Publishing in Topic {} a new Message {}", topic, payload);

        pubSubTemplate.publish(topic, payload);
    }

}
