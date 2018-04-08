package com.udacity.gradle.builditbigger.Profile.UserCollections;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Collection;

/**
 * DEPRECATED
 */

public class SearchUserCollectionLiveData extends LiveData<Collection> {
    private DatabaseReference databaseReference;
    private String[] tags;

    public SearchUserCollectionLiveData(String uid, String[] tags){
        databaseReference = Constants.DATABASE.child("usercollections/" + uid);
        this.tags = tags;
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String title = dataSnapshot.getValue(String.class);
            for (String tag: tags){
                if (title.contains(tag)) setValue(dataSnapshot.getValue(Collection.class));
            }
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
        databaseReference.addChildEventListener(childEventListener);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        databaseReference.removeEventListener(childEventListener);
        super.onInactive();
    }
}