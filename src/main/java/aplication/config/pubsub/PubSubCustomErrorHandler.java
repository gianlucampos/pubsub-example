package aplication.config.pubsub;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;

@Slf4j
@Configuration
@AllArgsConstructor
public class PubSubCustomErrorHandler {

    @Bean
    public MessageChannel errorEntryChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = "errorEntryChannel")
    public void pubsubErrorHandler(final Message<MessagingException> exceptionMessage) {
        log.error("Deu pau {}", exceptionMessage);
        Optional.ofNullable(exceptionMessage)
            .map(Message::getPayload)
            .map(MessagingException::getFailedMessage)
            .map(Message::getHeaders)
            .map(e -> e.get(GcpPubSubHeaders.ORIGINAL_MESSAGE))
            .map(message -> (BasicAcknowledgeablePubsubMessage) message)
            .ifPresent(BasicAcknowledgeablePubsubMessage::ack);
    }

}
