package com.udacity.gradle.builditbigger.Models;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * TranscriptPreview class serves as model for sent messages preview
 */

public class TranscriptPreview {

    private Message message;
    private List<HilarityUser> conversationalists;
    private String path;
    private Boolean original;
    private Boolean hasUnreadMessages;

    public TranscriptPreview(){}

    public TranscriptPreview(Message message, List<HilarityUser> conversationalists, String path, Boolean original, Boolean hasUnreadMessages){
        this.message = message;
        this.conversationalists = conversationalists;
        this.path = path;
        this.original = original;
        this.hasUnreadMessages = hasUnreadMessages;
    }

    public Message getMessage() {
        return message;
    }

    public List<HilarityUser> getConversationalists() {
        return conversationalists;
    }

    public String getPath() {return path;}

    public Boolean getOriginal() {return original;}

    public Boolean getHasUnreadMessages() {return hasUnreadMessages;}

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TranscriptPreview) && ((TranscriptPreview) obj).getPath().equals(path);
    }

    public boolean compare(@NonNull TranscriptPreview transcriptPreview){
        return  message.equals(transcriptPreview.getMessage());
    }
}
