package pl.borysfan;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Value("${websocket.activemq.hostname}")
    private String relayHost;

    @Value("${websocket.activemq.port:61613}")
    private int relayPort;

    @Value("${websocket.activemq.login}")
    private String clientLogin;

    @Value("${websocket.activemq.password}")
    private String clientPasscode;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayPort(relayPort)
                .setRelayHost(relayHost)
                .setClientLogin(clientLogin)
                .setClientPasscode(clientPasscode);
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp-endpoint").withSockJS();
    }
}
