package hu.codehunters.messages.controller;

import hu.codehunters.messages.model.Message;
import hu.codehunters.messages.model.MessagesContainer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    private final MessagesContainer messages = new MessagesContainer();

    /**
     * adds a new message
     * @return
     */
    @PostMapping(value = "/messages")
    public ResponseEntity<?> addMessageToList(@RequestBody Message message) {

        messages.addMessage(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * returns the list of the added messages
     * @return
     */
    @GetMapping("/messages")
    public List<Message> showMessages() {
        return messages.getMessages();
    }
}
