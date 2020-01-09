package com.udacity.gradle.builditbigger.profile.userPosts;

import android.os.Handler;
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
 * UserPostsLiveData class provides references to user generated posts
 */
//are the use of handlers necessary? yes, to keep from doing extra network request
public class UserPostsLiveData extends LiveData<PostWrapper> {
    private Query databaseReference;
    private double startAt;
    private String uid;

    public UserPostsLiveData(String uid) {
        databaseReference = Constants.DATABASE.child("userposts/" + uid + "/posts")
                .orderByChild("inverseTimeStamp").startAt(Constants.INVERSE / System.currentTimeMillis())
                .limitToFirst(20);
        this.uid = uid;
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
            setValue(new PostWrapper(dataSnapshot.getValue(Post.class), PostWrapper.NEW));


        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setValue(new PostWrapper(dataSnapshot.getValue(Post.class), PostWrapper.EDITTED));
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
            Log.i("hilarityApp", "onChildMoved called");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
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

    public void setStartAt(double startAt) {
        this.startAt = startAt;
    }

    public void newQuery() {
        databaseReference = Constants.DATABASE.child("userposts/" + uid + "/posts").orderByChild("inverseTimeStamp").startAt(startAt).limitToFirst(20);
        Log.i("new_query", "new query called");
        onActive();
    }
}
