package com.udacity.gradle.builditbigger.Feed;

import android.arch.lifecycle.LiveData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.Models.PostWrapper;

/**
 * retrieves post for user feed
 */

public class FeedLiveData extends LiveData<PostWrapper> {
    Query query;

    public FeedLiveData(String uid){
        query = Constants.DATABASE.child("feeds/"+uid);//.limitToLast(30);endAt();
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            setValue(new PostWrapper(dataSnapshot.getValue(Post.class),1));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setValue(new PostWrapper(dataSnapshot.getValue(Post.class),2));
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            setValue(new PostWrapper(dataSnapshot.getValue(Post.class),3));
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };

    @Override
    protected void onActive() {
        super.onActive();
        query.addChildEventListener(childEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        query.removeEventListener(childEventListener);
    }
}
