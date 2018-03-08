package com.udacity.gradle.builditbigger.Models;

import java.util.List;

/**
 * Created by joeljohnson on 1/31/18.
 */

public class TranscriptPreview {

    private Message message;
    private List<String> conversationalists;


    public TranscriptPreview(Message messages, List<String> conversationalists){
        message = messages;
        this.conversationalists = conversationalists;
    }

    public Message getMessage() {
        return message;
    }

    public List<String> getConversationalists() {
        return conversationalists;
    }

}
