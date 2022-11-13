package aplication.pubsub.subscribe;

import domain.entities.PayloadMessageExample;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ExampleSubscribe {

    @ServiceActivator(inputChannel = "exampleChannel")
    public void subscribeExample(final PayloadMessageExample payload) {
        log.info("new Message received, {}", payload);
    }

}
