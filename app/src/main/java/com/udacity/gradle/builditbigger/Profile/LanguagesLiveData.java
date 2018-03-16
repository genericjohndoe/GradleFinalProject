package com.udacity.gradle.builditbigger.Profile;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;

import java.util.List;

/**
 * LanguagesLiveData Class used to provide all the languages supported by the app
 */

public class LanguagesLiveData extends LiveData {

    private DatabaseReference databaseReference;

    public LanguagesLiveData(){
        databaseReference = Constants.DATABASE.child("Languages");
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //todo remember to cast to List<String>
            setValue(dataSnapshot.getValue());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };

    /*private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            setValue(dataSnapshot.getValue(String.class));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {}

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };*/

    @Override
    protected void onActive() {
        //databaseReference.addChildEventListener(childEventListener);
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        //databaseReference.removeEventListener(childEventListener);
        databaseReference.removeEventListener(valueEventListener);
        super.onInactive();
    }
}
