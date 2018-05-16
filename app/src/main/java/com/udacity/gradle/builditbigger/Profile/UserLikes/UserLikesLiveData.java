package com.udacity.gradle.builditbigger.Profile.UserLikes;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.Models.PostWrapper;

/**
 * UserLikesLiveData class retrieves liked post from database
 */

public class UserLikesLiveData extends LiveData<PostWrapper> {
    DatabaseReference databaseReference;

    public UserLikesLiveData(String uid){
        databaseReference = Constants.DATABASE.child("userlikes/" + uid + "/list");
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            setValue(new PostWrapper(dataSnapshot.getValue(Post.class), Constants.STATE_ADDED));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setValue(new PostWrapper(dataSnapshot.getValue(Post.class), Constants.STATE_CHANGED));
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            setValue(new PostWrapper(dataSnapshot.getValue(Post.class), Constants.STATE_REMOVED));
        }

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
