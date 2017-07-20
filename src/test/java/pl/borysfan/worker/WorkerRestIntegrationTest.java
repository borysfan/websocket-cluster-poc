package pl.borysfan.worker;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import pl.borysfan.socket.client.Credentials;
import pl.borysfan.socket.client.Subscription;
import pl.borysfan.socket.client.SynchronizationHandler;
import pl.borysfan.socket.client.TestWebSocketClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkerRestIntegrationTest {
    @Autowired
    private RestTemplate restTemplate;


    private Credentials credentials = new Credentials("user", "test");

    @LocalServerPort
    private int port;

    @Test
    public void shouldCallHelloEndpoint() {
        ResponseEntity<String> responseEntity = doGet("/api/hello", String.class);

        Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assert.assertEquals("Hello world", responseEntity.getBody());
    }

    @Test
    public void shouldNotifyAllActiveWebSocketClients() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Subscription subscription = new Subscription("/topic/activities", new SynchronizationHandler<>(new WorkerDtoHandler(), countDownLatch));
        try {
            new TestWebSocketClient(port).connect(credentials, subscription);
            if (subscription.isSubscribed()) {
                ResponseEntity<Void> responseEntity = doGet("/api/all/notifications", Void.class);
                Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
                Assert.assertTrue(countDownLatch.await(5, TimeUnit.SECONDS));
            }
        } finally {
            subscription.close();
        }

    }

    @Test
    public void shouldNotifyParticularUser() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Subscription subscription = new Subscription("/user/queue/activities", new SynchronizationHandler<>(new WorkerDtoHandler(), countDownLatch));
        try {
            new TestWebSocketClient(port).connect(credentials, subscription);
            if (subscription.isSubscribed()) {
                ResponseEntity<Void> responseEntity = doGet("/api/single/notifications", Void.class);
                Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
                Assert.assertTrue(countDownLatch.await(5, TimeUnit.SECONDS));
            }
        } finally {
            subscription.close();
        }
    }

    @Test
    public void shouldNotifyParticularUserDefinedByClient() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Subscription subscription = new Subscription("/user/queue/activities", new SynchronizationHandler<>(new WorkerDtoHandler(), countDownLatch));
        try {
            new TestWebSocketClient(port).connect(credentials, subscription);
            if (subscription.isSubscribed()) {
                ResponseEntity<Void> responseEntity = doGet("/api/single/notifications/user", Void.class);
                Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
                Assert.assertTrue(countDownLatch.await(5, TimeUnit.SECONDS));
            }
        } finally {
            subscription.close();
        }
    }

    private <T> ResponseEntity<T> doGet(String url, Class<T> clazz) {
        return restTemplate.exchange("http://localhost:" + port + url, HttpMethod.GET, new HttpEntity<>(credentials.httpHeaders()), clazz);
    }

}
