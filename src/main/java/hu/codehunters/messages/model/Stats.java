package hu.codehunters.messages.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Stats implements Serializable {
    @JsonProperty("posted_messages")
    private final int postedMessages;
    @JsonProperty("average_length")
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

    public int getPostedMessages() {
        return postedMessages;
    }

    public double getAverageLength() {
        return averageLength;
    }

    public List<WordOccurrence> getOccurrences() {
        return new ArrayList<>(occurrences);
    }
}
