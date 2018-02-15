package com.udacity.gradle.builditbigger.Models;

import java.util.List;

/**
 * Created by joeljohnson on 1/31/18.
 */

public class TranscriptPreview {

    private List<Message> messagelist;
    private List<String> conversationalists;
    private String timestampString;
    private Long timeInMillis;

    public TranscriptPreview(List<Message> messages, List<String> conversationalists, String timestampString, Long timeInMillis){
        messagelist = messages;
        this.conversationalists = conversationalists;
        this.timestampString = timestampString;
        this.timeInMillis = timeInMillis;
    }

    public List<Message> getMessagelist() {
        return messagelist;
    }

    public List<String> getConversationalists() {
        return conversationalists;
    }

    public Long getTimeInMillis() {
        return timeInMillis;
    }

    public String getTimestampString() {
        return timestampString;
    }
}
