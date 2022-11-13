package aplication.config.pubsub;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gcp.pubsub")
@Getter
@Setter
public class PubSubProperties {

    private Map<String, String> topics;
    private Map<String, String> subscriptions;

}
