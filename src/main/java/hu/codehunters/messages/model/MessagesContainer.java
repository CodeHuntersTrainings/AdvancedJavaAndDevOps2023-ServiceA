package hu.codehunters.messages.model;

import java.util.ArrayList;
import java.util.List;

public class MessagesContainer {

    private final List<Message> messages = new ArrayList<>();

    private final Object readLock = new Object();

    public void addMessage(Message message) {
        synchronized (messages) {
            messages.add(message);
            messages.notifyAll();
        }
    }

    public List<Message> getMessages() {
        List<Message> readMessages;
        synchronized (readLock) {
            readMessages = messages;
            readLock.notifyAll();
            return readMessages;
        }
    }
}
