package com.udacity.gradle.builditbigger.Profile.UserPosts;

import android.arch.lifecycle.LiveData;
import android.os.Handler;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Post;

/**
 * UserPostsLiveData class provides references to user generated posts
 */
//are the use of handlers necessary? yes, to keep from doing extra network request
public class UserPostsLiveData extends LiveData<Post> {
    private DatabaseReference databaseReference;

    public UserPostsLiveData(String uid){
        databaseReference = Constants.DATABASE.child("userposts/" + uid + "/posts");
    }

    private boolean listenerRemovePending = false;
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            databaseReference.removeEventListener(childEventListener);
            listenerRemovePending = false;
        }
    };
    private final Handler handler = new Handler();
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            setValue(dataSnapshot.getValue(Post.class));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setValue(dataSnapshot.getValue(Post.class));
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {}

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };

    @Override
    protected void onActive() {
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        } else {
            databaseReference.addChildEventListener(childEventListener);
        }
        listenerRemovePending = false;
        super.onActive();
    }

    @Override
    protected void onInactive() {
        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
        super.onInactive();
    }
}
