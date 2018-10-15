package com.udacity.gradle.builditbigger.subscribersSubsrciptions;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.models.HilarityUser;

public class SubsLiveData extends LiveData<HilarityUser> {
    private Query query;
    private String path;
    private String startAt;

    public SubsLiveData(String uid, boolean getFollowers){
        if (getFollowers){
            path = "followers/" + uid + "/list";
        } else {
            path = "following/" + uid + "/list";
        }
        query = Constants.DATABASE.child(path).orderByChild("userName").limitToFirst(20);
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
        query.addChildEventListener(childEventListener);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        query.removeEventListener(childEventListener);
        super.onInactive();
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public void newQuery(){
        query = Constants.DATABASE.child(path).orderByChild("userName")
                .startAt(startAt).limitToFirst(20);
        onActive();
    }
}
