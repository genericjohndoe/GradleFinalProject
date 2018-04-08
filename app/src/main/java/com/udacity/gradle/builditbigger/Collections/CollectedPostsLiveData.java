package com.udacity.gradle.builditbigger.Collections;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Post;

/**
 * Created by joeljohnson on 4/7/18.
 */

public class CollectedPostsLiveData extends LiveData<Post> {
    DatabaseReference databaseReference;

    public CollectedPostsLiveData(String collectionId){
        databaseReference = Constants.DATABASE.child("usercollections/"+Constants.UID+"/"+collectionId+"/posts");
    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            setValue(dataSnapshot.getValue(Post.class));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        databaseReference.addChildEventListener(childEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        databaseReference.removeEventListener(childEventListener);
    }
}
