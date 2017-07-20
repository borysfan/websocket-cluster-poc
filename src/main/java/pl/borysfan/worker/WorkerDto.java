package pl.borysfan.worker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkerDto {

    private String name;
    private String dateTime;

    public WorkerDto() {
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

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WorkerDto{" +
                "name='" + name + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
