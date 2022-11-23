package aplication.config.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.batching.FlowControlSettings;
import com.google.api.gax.batching.FlowController;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import domain.entities.PayloadMessageExample;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubMessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PubSubExampleConfig {

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    private final PubSubProperties pubSubProperties;
    private final PubSubTemplate pubSubTemplate;

    @Bean
    @InboundChannelAdapter(channel = "exampleChannel", poller = @Poller(fixedDelay = "100"))
    public MessageSource<Object> pubsubAdapter() {

        PubSubMessageSource messageSource = new PubSubMessageSource(
            pubSubTemplate,
            pubSubProperties.getSubscriptions().get("exampleSubscription")
        );
        messageSource.setAckMode(AckMode.MANUAL);
        messageSource.setPayloadType(PayloadMessageExample.class);
        return messageSource;
    }

    @Bean
    public Subscriber example() {

        final ProjectSubscriptionName subscription = ProjectSubscriptionName.of(
            projectId,
            pubSubProperties.getSubscriptions().get("exampleSubscription"));

        MessageReceiver receiver = (PubsubMessage message, AckReplyConsumer consumer) -> {
            try {
                new ObjectMapper().readValue(message.getData().toStringUtf8(), PayloadMessageExample.class);
                var numberAttempt = Subscriber.getDeliveryAttempt(message);
                log.info("Attempt number {}", numberAttempt);
            } catch (JsonProcessingException e) {
                log.error("Error at converting json to object {}", PayloadMessageExample.class, e);
                consumer.ack();
            }
        };

        FlowControlSettings flowControlSettings =
            FlowControlSettings.newBuilder()
                .setMaxOutstandingElementCount(2000L)
                .setLimitExceededBehavior(FlowController.LimitExceededBehavior.Block)
                .setMaxOutstandingRequestBytes(100L * 1024L * 1024L)
                .build();

        Subscriber subscriber = Subscriber.newBuilder(subscription, receiver)
            .setFlowControlSettings(flowControlSettings)
            .build();
        subscriber.startAsync().awaitRunning();
        return subscriber;
    }


}
