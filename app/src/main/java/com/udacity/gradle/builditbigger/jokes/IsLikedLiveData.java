package com.udacity.gradle.builditbigger.jokes;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.constants.Constants;

/**
 * class retrieves boolean indicating whether user liked post
 */

public class IsLikedLiveData extends LiveData<Boolean> {
    private DatabaseReference databaseReference;

    public IsLikedLiveData(String uid, String pushId){
        databaseReference = Constants.DATABASE.child("userpostslikescomments/" + uid + "/" + pushId + "/likes/list/" + Constants.UID);
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            setValue(dataSnapshot.exists());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
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