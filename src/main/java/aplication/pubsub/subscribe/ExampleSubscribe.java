package aplication.pubsub.subscribe;

import static org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders.ORIGINAL_MESSAGE;

import domain.entities.PayloadMessageExample;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.support.AcknowledgeablePubsubMessage;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ExampleSubscribe {

    @ServiceActivator(inputChannel = "exampleChannel")
    public void subscribeExample(
        @Header(ORIGINAL_MESSAGE) AcknowledgeablePubsubMessage message,
        final PayloadMessageExample payload) {

        try {
            log.info("[PUB/SUB] - Received message subscribeExample, payload {}", payload);
            message.ack();
            log.info("[PUB/SUB] - [ACK] Received message subscribeExample, payload {}", payload);
        } catch (Exception ex) {
            log.error("[PUB/SUB] - Error [NACK] subscribeExample", ex);
        }
    }

}
