package pl.borysfan.socket.client;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.Collections;

import static java.util.Arrays.asList;

public class TestWebSocketClient extends WebSocketStompClient {
    private final int port;
    private final String host;

    public TestWebSocketClient(int port) {
        this("localhost",port, new SockJsClient(Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))), new MappingJackson2MessageConverter());
    }

    public TestWebSocketClient(String host, int port) {
        this(host, port, new SockJsClient(Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))), new MappingJackson2MessageConverter());
    }

    public TestWebSocketClient(int port, RestTemplate restTemplate) {
        this("localhost", port, new SockJsClient(asList(new WebSocketTransport(new StandardWebSocketClient()), new RestTemplateXhrTransport(restTemplate))), new MappingJackson2MessageConverter());
    }

    public TestWebSocketClient(String host, int port, WebSocketClient webSocketClient, MessageConverter messageConverter) {
        super(webSocketClient);
        this.setMessageConverter(messageConverter);
        this.port = port;
        this.host = host;
    }

    public void connect(StompSessionHandler handler) {
        connect(new WebSocketHttpHeaders(), handler);
    }

    public void connect(Credentials credentials, StompSessionHandler handler) {
        connect(new WebSocketHttpHeaders(credentials.httpHeaders()), handler);
    }

    public void connect(WebSocketHttpHeaders webSocketHttpHeaders, StompSessionHandler handler) {
        this.connect("ws://{host}:{port}/stomp-endpoint", webSocketHttpHeaders, handler, host, port);
    }
}
