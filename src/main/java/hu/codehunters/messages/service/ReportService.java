package hu.codehunters.messages.service;

import hu.codehunters.messages.model.Message;
import hu.codehunters.messages.model.Stats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final MessageService messageService;

    @Autowired
    public ReportService(@Qualifier("messageService") MessageService messageService) {
        this.messageService = messageService;
    }

    public Stats getStats() {
        List<Message> messagesInLastMinute = getMessagesLastMinute();
        return new Stats(
                getNumberOfMessagesLastMinute(),
                getAverageLengthOfUniqueWordsInMessages(messagesInLastMinute).orElse(0d),
                getWordOccurrences(messagesInLastMinute));
    }


    private List<Message> getMessagesLastMinute() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteEarlier = now.minusMinutes(1L);

        return getMessagesCreatedFromTo(oneMinuteEarlier, now);
    }

    private int getNumberOfMessagesLastMinute() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteEerlier = now.minusMinutes(1L);

        return (int) messageService.getMessages()
                .stream()
                .filter(
                        m -> m.getCreationDateTime().isAfter(oneMinuteEerlier) &&
                                m.getCreationDateTime().isBefore(now))
                .count();
    }

    private OptionalDouble getAverageLengthOfUniqueWordsInMessages(List<Message> messages) {
        return messages.stream()
                .map(Message::getMessage)
                .map(m -> m.split("[\\w]"))
                .flatMap(Arrays::stream)
                .mapToDouble(String::length)
                .average();
    }

    private List<Message> getMessagesCreatedFromTo(LocalDateTime from, LocalDateTime to) {
        return messageService.getMessages()
                .stream()
                .filter(m -> m.getCreationDateTime().isAfter(from) && m.getCreationDateTime().isBefore(to))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> getWordOccurrences(List<Message> messages) {
        return messages.stream()
                .map(Message::getMessage)
                .map(m -> m.split("[\\w]"))
                .flatMap(Arrays::stream)
                .collect(Collectors.toMap(
                        k -> k,
                        v -> 1,
                        Integer::sum,
                        HashMap::new
                ));
    }


}
