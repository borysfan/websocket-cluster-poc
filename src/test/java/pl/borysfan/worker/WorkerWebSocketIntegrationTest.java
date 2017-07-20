package pl.borysfan.worker;

import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.borysfan.socket.client.Credentials;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkerWebSocketIntegrationTest {

    private Credentials credentials = new Credentials("user", "test");

    @LocalServerPort
    private int port;


}
