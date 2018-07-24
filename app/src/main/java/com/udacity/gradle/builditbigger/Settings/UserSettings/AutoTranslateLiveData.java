package com.udacity.gradle.builditbigger.Settings.UserSettings;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;

public class AutoTranslateLiveData extends LiveData<Boolean> {

    private DatabaseReference db;

    public AutoTranslateLiveData(){
        db = Constants.DATABASE.child("cloudsettings/"+Constants.UID+"/autotranslate");
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot.getValue(Boolean.class));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
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
