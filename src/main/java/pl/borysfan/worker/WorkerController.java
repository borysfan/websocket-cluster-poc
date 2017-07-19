package pl.borysfan.worker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkerController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello world";
    }
}
