package hu.codehunters.messages.service;

import hu.codehunters.messages.model.Message;
import hu.codehunters.messages.model.MessagesContainer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessagesContainer messages = new MessagesContainer();

    public void addMessage(Message message) {
        messages.addMessage(message);
    }

    public List<Message> getMessages() {
        return messages.getMessages();
    }

}
