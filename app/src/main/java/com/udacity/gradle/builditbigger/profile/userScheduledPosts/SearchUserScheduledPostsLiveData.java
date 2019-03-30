package com.udacity.gradle.builditbigger.profile.userScheduledPosts;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.models.Post;
import com.udacity.gradle.builditbigger.models.PostWrapper;

public class SearchUserScheduledPostsLiveData extends LiveData<PostWrapper> {
    private Query query;
    //todo won't work in current state, prolly should use firestore
    public SearchUserScheduledPostsLiveData(String tag){
        query = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts")
                .orderByChild("metaData/" + tag).equalTo(true);
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            setValue(new PostWrapper(dataSnapshot.getValue(Post.class),1));
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
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
