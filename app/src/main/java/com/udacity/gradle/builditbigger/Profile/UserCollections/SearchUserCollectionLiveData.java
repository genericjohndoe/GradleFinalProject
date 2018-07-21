package com.udacity.gradle.builditbigger.Profile.UserCollections;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Collection;


/**
 * Returns filtered list of collections
 */

public class SearchUserCollectionLiveData extends LiveData<Collection> {
    private Query query;

    public SearchUserCollectionLiveData(String uid, String tag){
        query = Constants.DATABASE.child("usercollections/" + uid).orderByChild("title").startAt(tag).endAt(tag+"z");

    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            setValue(dataSnapshot.getValue(Collection.class));
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
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
