package com.udacity.gradle.builditbigger.messaging.sentMessages;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.models.TranscriptPreview;

/**
 * Created by joeljohnson on 1/31/18.
 */

public class SentMessagesLiveData extends LiveData<TranscriptPreview> {
    private Query databaseReference;

    public SentMessagesLiveData(String uid){
        databaseReference = Constants.DATABASE.child("transcriptpreviews/"+uid).orderByChild("message/timeStamp");
    }


    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            setValue(dataSnapshot.getValue(TranscriptPreview.class));
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
            setValue(dataSnapshot.getValue(TranscriptPreview.class));
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    @Override
    protected void onActive() {
        databaseReference.addChildEventListener(childEventListener);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        databaseReference.removeEventListener(childEventListener);
        super.onInactive();
    }
}
