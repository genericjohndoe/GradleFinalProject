package com.udacity.gradle.builditbigger.database;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.models.HilarityUser;

/**
 * Created by joeljohnson on 2/13/18.
 */

public class HilarityUserLiveData extends LiveData {

    private DatabaseReference databaseReference;
    private HilarityUserDao hilarityUserDao;

    public HilarityUserLiveData(HilarityUserDatabase hilarityUserDatabase){
        databaseReference = Constants.DATABASE.child("users");
        hilarityUserDao = hilarityUserDatabase.dao();
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            hilarityUserDao.insert(dataSnapshot.getValue(HilarityUser.class));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            hilarityUserDao.update(dataSnapshot.getValue(HilarityUser.class));
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            hilarityUserDao.delete(dataSnapshot.getValue(HilarityUser.class));
        }

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