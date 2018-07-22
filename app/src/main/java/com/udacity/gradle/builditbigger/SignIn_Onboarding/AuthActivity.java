package com.udacity.gradle.builditbigger.SignIn_Onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;

public class AuthActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    CircularProgressView circularProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        circularProgressView = findViewById(R.id.progress);
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
        circularProgressView.startAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) auth.removeAuthStateListener(mAuthStateListener);
        circularProgressView.stopAnimation();
    }

    public void configureApp(FirebaseUser user) {
        Constants.UID = user.getUid();
        Constants.DATABASE.child("users/" + Constants.UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Constants.USER = dataSnapshot.getValue(HilarityUser.class);
                if (Constants.USER != null) {
                    startActivity(new Intent(getBaseContext(), HilarityActivity.class));
                } else {
                    startActivity(new Intent(AuthActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
