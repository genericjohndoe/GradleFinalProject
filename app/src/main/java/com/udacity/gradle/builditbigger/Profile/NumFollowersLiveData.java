package com.udacity.gradle.builditbigger.Profile;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;

/**
 * Created by joeljohnson on 1/20/18.
 */

public class NumFollowersLiveData extends LiveData<Long> {
    private DatabaseReference databaseReference;

    public NumFollowersLiveData(String uid){
        databaseReference = Constants.DATABASE.child("followers/" + Constants.UID + "/num");
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            setValue(dataSnapshot.getValue(Long.class));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {}
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
