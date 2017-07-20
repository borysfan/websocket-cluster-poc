package pl.borysfan.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class WorkerRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerRestController.class);

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WorkerRestController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello world";
    }

    @GetMapping("/api/all/notifications")
    public void notifyAllAboutUserActivity(Principal principal){
        LOGGER.info("/api/all/notifications has endpoint has been called by {}", principal.getName());
        simpMessagingTemplate.convertAndSend("/topic/activities", new WorkerDto(principal.getName()));
    }

    @GetMapping("/api/single/notifications")
    public void notifyAboutUserActivity(Principal principal) {
        LOGGER.info("/api/single/notifications has endpoint has been called by {}", principal.getName());
        simpMessagingTemplate.convertAndSendToUser(principal.getName(),"/queue/activities", new WorkerDto(principal.getName()));
    }

    @GetMapping("/api/single/notifications/{username}")
    public void notifyAboutUserActivity(@PathVariable String username) {
        LOGGER.info("/api/single/notifications/{} has endpoint has been called", username);
        simpMessagingTemplate.convertAndSendToUser(username,"/queue/activities", new WorkerDto("single notification"));
    }

}
