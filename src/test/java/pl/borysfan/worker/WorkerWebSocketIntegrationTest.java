package pl.borysfan.worker;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.borysfan.socket.client.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkerWebSocketIntegrationTest {

    private Credentials credentials = new Credentials("user", "test");

    @LocalServerPort
    private int port;

    @Test
    public void shouldNotifyAllUsers() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Subscription subscription = new Subscription("/topic/ws-activities", new SynchronizationHandler<>(new WorkerDtoHandler(), countDownLatch));
        try {
            new TestWebSocketClient(port).connect(
                    credentials,
                    new Connection(
                            subscription,
                            stompSession -> stompSession.send(
                                    "/app/ws-all-notifications",
                                    null)
                    )
            );
            Assert.assertTrue(countDownLatch.await(5, TimeUnit.SECONDS));
        } finally {
            subscription.close();
        }
    }

    @Test
    public void shouldNotifySingleUser() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Subscription subscription = new Subscription("/user/queue/ws-activities", new SynchronizationHandler<>(new WorkerDtoHandler(), countDownLatch));
        try {
            new TestWebSocketClient(port).connect(
                    credentials,
                    new Connection(
                            subscription,
                            stompSession -> stompSession.send(
                                    "/app/ws-single-notifications",
                                    null)
                    )
            );
            Assert.assertTrue(countDownLatch.await(5, TimeUnit.SECONDS));
        } finally {
            subscription.close();
        }
    }
}
