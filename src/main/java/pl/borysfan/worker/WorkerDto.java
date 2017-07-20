package pl.borysfan.worker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WorkerDto {

    private String name;
    private List<String> sessionIds;
    private String dateTime;

    public WorkerDto() {
    }

    public WorkerDto(String name, List<String> sessionIds) {
       this(name);
       this.sessionIds = sessionIds;
    }

    public WorkerDto(String name){
       this.name = name;
       this.dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }

    public String getName() {
        return name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSessionIds() {
        return sessionIds;
    }

    public void setSessionIds(List<String> sessionIds) {
        this.sessionIds = sessionIds;
    }

    @Override
    public String toString() {
        return "WorkerDto{" +
                "name='" + name + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", sessionIds='" + sessionIds +'\'' +
                '}';
    }
}
