package pl.borysfan.main;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.borysfan.socket.client.Credentials;
import pl.borysfan.socket.client.Subscription;
import pl.borysfan.socket.client.SynchronizationHandler;
import pl.borysfan.socket.client.TestWebSocketClient;
import pl.borysfan.worker.WorkerDtoHandler;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class SocketMain {

    private static int port = 8082;
    private static Credentials credentials = new Credentials("user", "test");

    private static final String QUEUE_ACTIVITIES = "/user/queue/activities";
    private static final String TOPIC_ACTIVITIES = "/topic/activities";

    public static void main(String[] args) throws InterruptedException, IOException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Subscription subscription = new Subscription(QUEUE_ACTIVITIES, new SynchronizationHandler<>(new WorkerDtoHandler(), countDownLatch));
        new TestWebSocketClient("192.168.99.100", port).connect(credentials, subscription);
        if (subscription.isSubscribed()) {
            ResponseEntity<Void> responseEntity = doGet("/api/single/notifications/user", Void.class);
            System.out.println("Response status code: " + responseEntity.getStatusCode());
        }
        System.in.read();
    }

    private static <T> ResponseEntity<T> doGet(String url, Class<T> clazz) {
        return new RestTemplate().exchange("http://192.168.99.100:" + port + url, HttpMethod.GET, new HttpEntity<>(credentials.httpHeaders()), clazz);
    }
}
