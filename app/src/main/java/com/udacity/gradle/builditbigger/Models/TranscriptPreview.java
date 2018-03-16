package com.udacity.gradle.builditbigger.Models;

import java.util.List;

/**
 * TranscriptPreview class serves as model for sent messages preview
 */

public class TranscriptPreview {

    private Message message;
    private List<String> conversationalists;
    private String path;

    public TranscriptPreview(){}


    public TranscriptPreview(Message message, List<String> conversationalists, String path){
        this.message = message;
        this.conversationalists = conversationalists;
        this.path = path;
    }

    public Message getMessage() {
        return message;
    }

    public List<String> getConversationalists() {
        return conversationalists;
    }

    public String getPath() {
        return path;
    }
}
