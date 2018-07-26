package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.HilarityUser;

/**
 * class retrieves list of user subscriptions
 */

public class SubscriptionsLiveData extends LiveData<HilarityUser> {
    private Query databaseReference;
    private String startAt;
    private String uid;

    public SubscriptionsLiveData(String uid){
        databaseReference = Constants.DATABASE.child("following/" + uid + "/list").orderByChild("userName").limitToFirst(20);
        this.uid = uid;
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            setValue(dataSnapshot.getValue(HilarityUser.class));
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
        databaseReference.addChildEventListener(childEventListener);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        databaseReference.removeEventListener(childEventListener);
        super.onInactive();
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public void newQuery(){
        databaseReference = Constants.DATABASE.child("following/" + uid + "/list").orderByChild("userName")
                .startAt(startAt).limitToFirst(20);
        onActive();
    }
}
