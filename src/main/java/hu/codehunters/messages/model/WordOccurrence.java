package hu.codehunters.messages.model;

import java.util.Map;
import java.util.Objects;

public class WordOccurrence {

    private final String word;
    private final int count;

    public WordOccurrence(String word, int count){
        this.word = word;
        this.count = count;
    }

    public WordOccurrence(Map.Entry<String, Integer> occurrence){
        this.word =  Objects.requireNonNull(occurrence.getKey());
        this.count = Objects.requireNonNull(occurrence.getValue());


    }
}
