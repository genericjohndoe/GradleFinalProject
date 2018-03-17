package com.udacity.gradle.builditbigger.Search.SearchCollections;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Genre;

/**
 * Created by joeljohnson on 3/16/18.
 */

public class SearchCollectionsLiveData extends LiveData<Genre> {

    DatabaseReference databaseReference = Constants.DATABASE.child("allcollections");

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            setValue(dataSnapshot.getValue(Genre.class));
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
        super.onActive();
        databaseReference.addChildEventListener(childEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        databaseReference.removeEventListener(childEventListener);
    }
}
