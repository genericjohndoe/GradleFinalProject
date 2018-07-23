package com.udacity.gradle.builditbigger.MainUI;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;

public class UnreadMessagesLiveData extends LiveData<Double> {
    DatabaseReference databaseReference;

    public UnreadMessagesLiveData(){
        databaseReference = Constants.DATABASE.child("messages/"+Constants.UID+"/numUnread");
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot.getValue(Double.class));
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
