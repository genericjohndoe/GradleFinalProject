package com.udacity.gradle.builditbigger.settings.userSettings;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.constants.Constants;

public class TaglineLiveData extends LiveData<String> {

    private DatabaseReference db;

    public TaglineLiveData(){
        db = Constants.DATABASE.child("users/"+Constants.UID+"/tagline");
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot.getValue(String.class));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    @Override
    protected void onActive() {
        super.onActive();
        db.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        db.removeEventListener(valueEventListener);
    }
}
