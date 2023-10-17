package hu.codehunters.messages.controller;

import hu.codehunters.messages.model.Message;
import hu.codehunters.messages.model.Stats;
import hu.codehunters.messages.service.MessageService;
import hu.codehunters.messages.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    private final MessageService messageService;
    private final ReportService reportService;

    @Autowired
    public MessageController(@Qualifier("messageService") MessageService messageService,
                             @Qualifier("reportService") ReportService reportService) {
        this.messageService = messageService;
        this.reportService = reportService;
    }

    /**
     * adds a new message
     * @return
     */
    @PostMapping(value = "/messages")
    public ResponseEntity<?> addMessageToList(@RequestBody Message message) {
        messageService.addMessage(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * returns the list of the added messages
     * @return
     */
    @GetMapping("/messages")
    public List<Message> showMessages() {
        return messageService.getMessages();
    }

    @GetMapping("/stats")
    public ResponseEntity<Stats> reportStats() {
        return new ResponseEntity<>(reportService.getStats(), HttpStatus.OK);
    }
}
