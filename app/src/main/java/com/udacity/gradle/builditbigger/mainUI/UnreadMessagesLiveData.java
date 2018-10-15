package com.udacity.gradle.builditbigger.mainUI;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.constants.Constants;

public class UnreadMessagesLiveData extends LiveData<Boolean> {
    private Query databaseReference;

    public UnreadMessagesLiveData(){
        databaseReference = Constants.DATABASE.child("transcriptpreviews/"+Constants.UID).orderByChild("hasUnreadMessages").equalTo(true);
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot.hasChildren());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    @Override
    protected void onActive() {
        super.onActive();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        databaseReference.removeEventListener(valueEventListener);
    }
}
