package pl.borysfan.worker;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class WorkerDtoHandler implements StompFrameHandler {

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return WorkerDto.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        WorkerDto workerDto = (WorkerDto) payload;
        System.out.println(workerDto);
    }

}