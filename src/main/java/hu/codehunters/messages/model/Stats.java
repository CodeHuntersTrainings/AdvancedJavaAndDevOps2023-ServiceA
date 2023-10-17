package hu.codehunters.messages.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Stats {
    private final int postedMessages;
    private final double averageLength;

    private final List<WordOccurrence> occurrences;

    public Stats(int postedMessages, double averageLength, List<WordOccurrence> occurrences) {
        this.postedMessages = postedMessages;
        this.averageLength = averageLength;
        this.occurrences = occurrences;
    }

    public Stats(int postedMessages, double averageLength, Map<String, Integer> occurrences) {
        this.postedMessages = postedMessages;
        this.averageLength = averageLength;
        this.occurrences = occurrenceListOf(occurrences);
    }


    private List<WordOccurrence> occurrenceListOf(Map<String, Integer> occurrences) {
        return occurrences.entrySet().stream().map(WordOccurrence::new).collect(Collectors.toList());
    }

}
