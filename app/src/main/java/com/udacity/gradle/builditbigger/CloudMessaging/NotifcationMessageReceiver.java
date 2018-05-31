package com.udacity.gradle.builditbigger.CloudMessaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;

import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Message;

import java.util.ArrayList;
import java.util.List;

public class NotifcationMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        CharSequence message = getReplyMessage(intent);
        if (message != null){
            String path = intent.getStringExtra("path");
            List<String> newMessage = new ArrayList<>();
            newMessage.add(""+message);
            DatabaseReference db = Constants.DATABASE.child("messages/"+Constants.UID+"/"+path+"/messagelist").push();
            Message message1 = new Message(Constants.USER, newMessage, System.currentTimeMillis(), db.getKey(),true);
            db.setValue(message1, (databaseError, databaseReference) -> {
               if (databaseError == null){
                   Constants.DATABASE.child("transcriptpreviews/"+Constants.UID+"/"+path+"/message")
                           .setValue(message1);
               }
            });
        }
        // an Intent broadcast.
    }

    private CharSequence getReplyMessage(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence("key_text_reply");
        }
        return null;
    }

}
