package com.udacity.gradle.builditbigger.isFollowing;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;

/**
 * CLASS responsible for seeing if user follows a given user
 */

public class IsFollowingLiveData extends LiveData<Boolean> {
    DatabaseReference databaseReference;

    public IsFollowingLiveData(String uid){
        databaseReference = Constants.DATABASE.child("followers/" + uid + "/list/"+ Constants.UID);
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {setValue(dataSnapshot.exists());}

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
