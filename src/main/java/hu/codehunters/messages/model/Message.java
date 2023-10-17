package hu.codehunters.messages.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class Message {

    private final String message;
    @JsonIgnore
    private final LocalDateTime creationDateTime;

    @JsonCreator
    public Message(@JsonProperty("message") String message) {
        this.message = message;
        this.creationDateTime = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }
    public LocalDateTime getCreationDateTime(){
        return creationDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return this.message.equals(message.message);
    }

    @Override
    public int hashCode() {
        return message.hashCode();
    }
}
