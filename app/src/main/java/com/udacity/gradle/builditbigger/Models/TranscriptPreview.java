package com.udacity.gradle.builditbigger.Models;

import java.util.List;

/**
 * TranscriptPreview class serves as model for sent messages preview
 */

public class TranscriptPreview {

    private Message message;
    private List<HilarityUser> conversationalists;
    private String path;
    private Boolean original;

    public TranscriptPreview(){}

    public TranscriptPreview(Message message, List<HilarityUser> conversationalists, String path, Boolean original){
        this.message = message;
        this.conversationalists = conversationalists;
        this.path = path;
        this.original = original;
    }

    public Message getMessage() {
        return message;
    }

    public List<HilarityUser> getConversationalists() {
        return conversationalists;
    }

    public String getPath() {
        return path;
    }

    public Boolean getOriginal() { return original;}

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TranscriptPreview) && ((TranscriptPreview) obj).getPath().equals(path);
    }
}
