package aplication.config.pubsub;

import domain.entities.PayloadMessageExample;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
@AllArgsConstructor
public class PubSubExampleConfig {

    private final PubSubProperties pubSubProperties;
    private final PubSubTemplate pubSubTemplate;
    private final MessageChannel errorEntryChannel;

    @Bean
    public MessageChannel exampleChannel() {
        return new DirectChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter exampleAdapter() {
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(
            pubSubTemplate, pubSubProperties.getSubscriptions().get("exampleSubscription")
        );

        adapter.setOutputChannel(exampleChannel());
        adapter.setAckMode(AckMode.AUTO_ACK);
        adapter.setPayloadType(PayloadMessageExample.class);
        adapter.setErrorChannel(errorEntryChannel);

        return adapter;
    }
}
