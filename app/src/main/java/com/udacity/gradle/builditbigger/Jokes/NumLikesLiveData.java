package com.udacity.gradle.builditbigger.Jokes;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;

/**
 * Class retrieves the number of likes on a post
 */

public class NumLikesLiveData extends LiveData<Long> {

    private DatabaseReference databaseReference;

    public NumLikesLiveData(String uid, String pushId){
        databaseReference = Constants.DATABASE.child("userpostslikescomments/" + uid + "/" + pushId + "/likes/num");
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
        super.onActive();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        databaseReference.removeEventListener(valueEventListener);
    }
}
