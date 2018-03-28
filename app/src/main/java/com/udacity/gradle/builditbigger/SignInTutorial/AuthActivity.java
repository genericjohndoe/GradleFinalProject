package com.udacity.gradle.builditbigger.SignInTutorial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.HilarityUser;

public class AuthActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.FIREBASEDATABASE = FirebaseDatabase.getInstance();
        Constants.DATABASE = Constants.FIREBASEDATABASE.getReference();
        mAuthStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                configureApp(user);
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) auth.removeAuthStateListener(mAuthStateListener);
    }

    public void configureApp(FirebaseUser user) {
        Constants.UID = user.getUid();
        Constants.DATABASE.child("users/" + Constants.UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Constants.USER = dataSnapshot.getValue(HilarityUser.class);
                if (Constants.USER != null) {
                    startActivity(new Intent(getBaseContext(), HilarityActivity.class));
                }else {
                    startActivity(new Intent(AuthActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
