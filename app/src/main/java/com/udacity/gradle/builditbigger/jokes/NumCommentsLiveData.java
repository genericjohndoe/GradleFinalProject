package com.udacity.gradle.builditbigger.jokes;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.constants.Constants;

/**
 * Class retrieves the number of comments on a post
 */

public class NumCommentsLiveData extends LiveData<Long> {
    private DatabaseReference databaseReference;

    public NumCommentsLiveData(String uid, String pushId){
        databaseReference = Constants.DATABASE.child("userpostslikescomments/" + uid + "/" + pushId + "/comments/num");
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
        super.onActive();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        databaseReference.removeEventListener(valueEventListener);
    }
}
