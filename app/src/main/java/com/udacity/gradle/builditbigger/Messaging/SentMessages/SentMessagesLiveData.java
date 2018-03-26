package com.udacity.gradle.builditbigger.Messaging.SentMessages;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.TranscriptPreview;

/**
 * Created by joeljohnson on 1/31/18.
 */

public class SentMessagesLiveData extends LiveData<TranscriptPreview> {
    private DatabaseReference databaseReference;

    public SentMessagesLiveData(String uid){
        databaseReference = Constants.DATABASE.child("messages/"+uid);
    }


    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            setValue(dataSnapshot.child("transcriptPreview").getValue(TranscriptPreview.class));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {}

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onCancelled(DatabaseError databaseError) {}
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