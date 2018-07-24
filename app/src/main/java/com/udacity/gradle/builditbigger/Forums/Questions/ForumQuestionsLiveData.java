package com.udacity.gradle.builditbigger.Forums.Questions;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.ForumQuestion;

/**
 * Created by joeljohnson on 3/31/18.
 */

public class ForumQuestionsLiveData extends LiveData<ForumQuestion> {

    private DatabaseReference databaseReference;

    public ForumQuestionsLiveData(){
        databaseReference = Constants.DATABASE.child("forumquestions");
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            setValue(dataSnapshot.getValue(ForumQuestion.class));
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
            setValue(dataSnapshot.getValue(ForumQuestion.class));
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
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
