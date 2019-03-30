package com.udacity.gradle.builditbigger.profile.userScheduledPosts;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.models.Post;
import com.udacity.gradle.builditbigger.models.PostWrapper;

public class UserScheduledPostsLiveData extends LiveData<PostWrapper> {
    private Query databaseReference;
    private double startAt;
    private String uid;

    public UserScheduledPostsLiveData(String uid){
        databaseReference = Constants.DATABASE.child("userposts/" + uid + "/posts")
                .orderByChild("inverseTimeStamp").endAt(Constants.INVERSE/System.currentTimeMillis())
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
            setValue(new PostWrapper(dataSnapshot.getValue(Post.class),1));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setValue(new PostWrapper(dataSnapshot.getValue(Post.class),2));
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
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

    public void newQuery(){
        databaseReference = Constants.DATABASE.child("userposts/" + uid + "/posts").orderByChild("inverseTimeStamp").endAt(startAt).limitToFirst(20);
        Log.i("new_query","new query called");
        onActive();
    }
}
