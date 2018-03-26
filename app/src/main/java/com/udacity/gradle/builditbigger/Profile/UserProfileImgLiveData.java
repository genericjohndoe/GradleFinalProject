package com.udacity.gradle.builditbigger.Profile;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;

/**
 * UserProfileImgLiveData class keeps track of the url to the user profile image
 */

public class UserProfileImgLiveData extends LiveData<String> {

    private DatabaseReference databaseReference;

    public UserProfileImgLiveData(String uid){
        databaseReference = Constants.DATABASE.child("users/"+uid+"/urlString");
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            setValue(dataSnapshot.getValue(String.class));
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