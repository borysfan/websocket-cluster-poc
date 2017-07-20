package pl.borysfan.worker;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WorkerWebSocketController {

    @MessageMapping("/ws-all-notifications")
    @SendTo("/topic/ws-activities")
    public WorkerDto notifyAll(Principal principal) {
        return new WorkerDto(principal.getName());
    }

    @MessageMapping("/ws-single-notifications")
    @SendToUser("/queue/ws-activities")
    public WorkerDto notifySingle(Principal principal) {
        return new WorkerDto(principal.getName());
    }

}
