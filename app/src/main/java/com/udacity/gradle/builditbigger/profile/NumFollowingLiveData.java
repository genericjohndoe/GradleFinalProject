package com.udacity.gradle.builditbigger.profile;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.constants.Constants;

/**
 * NumFollowingLiveData class keeps reference to database path with the number of users followed by
 * a given user
 */

public class NumFollowingLiveData extends LiveData<Long> {
    private DatabaseReference databaseReference;

    public NumFollowingLiveData(String uid){
        databaseReference = Constants.DATABASE.child("following/" + uid + "/num");
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            setValue(dataSnapshot.getValue(Long.class));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    @Override
    protected void onActive() {
        databaseReference.addValueEventListener(valueEventListener);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        databaseReference.removeEventListener(valueEventListener);
        super.onInactive();
    }
}
