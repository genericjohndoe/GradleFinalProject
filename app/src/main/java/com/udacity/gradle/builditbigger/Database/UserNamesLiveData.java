package com.udacity.gradle.builditbigger.Database;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Profile.UserLikes.UserLikesLiveData;

/**
 * Created by joeljohnson on 2/8/18.
 */

public class UserNamesLiveData extends LiveData<UserName> {
    DatabaseReference databaseReference;
    UserNameDAO userNameDAO;

    public UserNamesLiveData(UserNameDatabase userNameDatabase){
        databaseReference = Constants.DATABASE.child("usernames");
        userNameDAO = userNameDatabase.dao();
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String value = dataSnapshot.getValue(String.class);
            String key = dataSnapshot.getKey();
            userNameDAO.insertUserName(new UserName(key, value));
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String value = dataSnapshot.getValue(String.class);
            String key = dataSnapshot.getKey();
            userNameDAO.updateUserName(new UserName(key, value));
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String value = dataSnapshot.getValue(String.class);
            String key = dataSnapshot.getKey();
            userNameDAO.deleteUserName(new UserName(key, value));
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
