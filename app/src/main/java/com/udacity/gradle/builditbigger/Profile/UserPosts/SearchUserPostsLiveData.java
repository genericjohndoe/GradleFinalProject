package com.udacity.gradle.builditbigger.Profile.UserPosts;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.Models.PostWrapper;

/**
 * SearchUserPostsLiveData class DEPRECATED
 */

public class SearchUserPostsLiveData extends LiveData<PostWrapper> {
    private Query query;

    public SearchUserPostsLiveData(String uid, String tag){
        query = Constants.DATABASE.child("userposts/" + uid + "/posts").orderByChild("metadata/" + tag).equalTo(true);

    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
           setValue(new PostWrapper(dataSnapshot.getValue(Post.class),1));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {}

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };

    @Override
    protected void onActive() {
        query.addChildEventListener(childEventListener);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        query.removeEventListener(childEventListener);
        super.onInactive();
    }
}
