package com.udacity.gradle.builditbigger.comments;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.models.Comment;

/**
 * retrieves comments from database
 */

public class CommentLiveData extends LiveData<Comment> {
    private DatabaseReference databaseReference;

    public CommentLiveData(String uid, String postId){
        databaseReference = Constants.DATABASE.child("userpostslikescomments/"+uid+"/"+postId+"/comments/commentlist");
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            setValue(dataSnapshot.getValue(Comment.class));
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
        super.onActive();
        databaseReference.addChildEventListener(childEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        databaseReference.removeEventListener(childEventListener);
    }
}

