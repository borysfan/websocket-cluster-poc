package pl.borysfan.socket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Subscription extends StompSessionHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Subscription.class);

    private final String destination;
    private final StompFrameHandler handler;
    private final CountDownLatch countDownLatch;
    private StompSession.Subscription subscription;
    private StompSession stompSession;

    public Subscription(String destination, StompFrameHandler handler) {
        this.destination = destination;
        this.handler = handler;
        this.countDownLatch = new CountDownLatch(1);
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        LOGGER.info("Connected. Subscribing to {}", destination);
        try {
            this.stompSession = session;
            subscription = session.subscribe(this.destination, handler);
        } finally {
            countDownLatch.countDown();
        }
    }

    public boolean isSubscribed() {
        try {
            return countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void close() {
        if (subscription != null) {
            LOGGER.info("Closing subscription: {}", subscription.getSubscriptionId());
            subscription.unsubscribe();
        }
    }

}
