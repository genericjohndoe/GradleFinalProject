package com.udacity.gradle.builditbigger.profile.userLikes;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.models.Post;
import com.udacity.gradle.builditbigger.models.PostWrapper;

/**
 * UserLikesLiveData class retrieves liked post from database
 */

public class UserLikesLiveData extends LiveData<PostWrapper> {
    private Query databaseReference;
    private double startAt;
    private String uid;

    public UserLikesLiveData(String uid){
        databaseReference = Constants.DATABASE.child("userlikes/" + uid + "/list").orderByChild("inverseTimeStamp").limitToFirst(20);
        this.uid = uid;
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
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
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

    public void setStartAt(double startAt) {
        this.startAt = startAt;
    }

    public void newQuery(){
        databaseReference = Constants.DATABASE.child("userlikes/" + uid + "/list").orderByChild("inverseTimeStamp").startAt(startAt).limitToFirst(20);
        Log.i("new_query","new query called");
        onActive();
    }
}
