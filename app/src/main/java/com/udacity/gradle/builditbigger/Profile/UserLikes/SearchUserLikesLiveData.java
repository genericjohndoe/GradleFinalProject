package com.udacity.gradle.builditbigger.Profile.UserLikes;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Joke;

/**
 * Created by joeljohnson on 1/22/18.
 */

public class SearchUserLikesLiveData extends LiveData<Joke> {
    DatabaseReference databaseReference;
    String[] tags;

    public SearchUserLikesLiveData(String uid, String[] tags){
        databaseReference = Constants.DATABASE.child("userlikes/" + uid + "/list");
        this.tags = tags;
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Iterable<DataSnapshot> snaps = dataSnapshot.child("taglist").getChildren();
            for (DataSnapshot snap: snaps){
                for (String tag: tags){
                    if (snap.getValue(String.class).equals(tag)){
                        setValue(dataSnapshot.getValue(Joke.class));
                    }
                }
            }
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
        databaseReference.addChildEventListener(childEventListener);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        databaseReference.removeEventListener(childEventListener);
        super.onInactive();
    }
}
