package pl.borysfan.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;

@RestController
public class WorkerRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerRestController.class);

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    @Autowired
    public WorkerRestController(SimpMessagingTemplate simpMessagingTemplate, SimpUserRegistry simpUserRegistry) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.simpUserRegistry = simpUserRegistry;
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello world";
    }

    @GetMapping("/api/all/notifications")
    public void notifyAllAboutUserActivity(Principal principal){
        LOGGER.info("/api/all/notifications has endpoint has been called by {}", principal.getName());
        simpMessagingTemplate.convertAndSend("/topic/activities", new WorkerDto(principal.getName(), getSessionIds(principal.getName())));
    }

    @GetMapping("/api/single/notifications")
    public void notifyAboutUserActivity(Principal principal) {
        LOGGER.info("/api/single/notifications has endpoint has been called by {}", principal.getName());
        simpMessagingTemplate.convertAndSendToUser(principal.getName(),"/queue/activities", new WorkerDto(principal.getName(), getSessionIds(principal.getName())));
    }

    @GetMapping("/api/single/notifications/{username}")
    public void notifyAboutUserActivity(@PathVariable String username) {
        LOGGER.info("/api/single/notifications/{} has endpoint has been called", username);
        simpMessagingTemplate.convertAndSendToUser(username,"/queue/activities", new WorkerDto("single notification " + simpUserRegistry.getUserCount()));
    }

    @GetMapping("/api/{username}/{sessionId}")
    public void assignSession(@PathVariable String username, @PathVariable String sessionId) {
        LOGGER.info("Assigning session {} for user {} ", sessionId, username);
    }

    private List<String> getSessionIds(String userName) {
        return Optional.ofNullable(simpUserRegistry.getUser(userName))
                .map(simpUser -> simpUser.getSessions().stream().map(SimpSession::getId).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }


}
