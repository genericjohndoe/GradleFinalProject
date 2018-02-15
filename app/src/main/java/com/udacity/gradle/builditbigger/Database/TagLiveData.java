package com.udacity.gradle.builditbigger.Database;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;

/**
 * Created by joeljohnson on 2/13/18.
 */

public class TagLiveData extends LiveData<Tag> {
    DatabaseReference databaseReference;
    TagDAO tagDAO;

    public TagLiveData(TagDatabase tagDatabase){
        databaseReference = Constants.DATABASE.child("taglist");
        tagDAO = tagDatabase.dao();
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            tagDAO.insert(new Tag(dataSnapshot.getKey()));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            tagDAO.delete(new Tag(dataSnapshot.getKey()));
        }

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
