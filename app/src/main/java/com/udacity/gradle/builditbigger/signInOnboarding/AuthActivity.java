package com.udacity.gradle.builditbigger.signInOnboarding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.mainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.models.HilarityUser;

/**
 * This activity checks to see if user is authorized to use the application
 */
public class AuthActivity extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    private CircularProgressView circularProgressView;
    private ImageView noSignal;
    //broadcast receiver acts as connectivity listener, changes UI accordingly
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                circularProgressView.startAnimation();
                noSignal.setVisibility(View.GONE);
            } else {
                noSignal.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        circularProgressView = findViewById(R.id.progress);
        noSignal = findViewById(R.id.imageView);
        Constants.FIREBASEDATABASE = FirebaseDatabase.getInstance();
        Constants.DATABASE = Constants.FIREBASEDATABASE.getReference();
        //anonymous class checks to see if user is logged in
        mAuthStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                //if user is logged in, app attempts to move to main UI
                configureApp(user);
            } else {
                //if user isn't logged in, app moves to login page
                startActivity(new Intent(this, LoginActivity.class));
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(mAuthStateListener);
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        //turn on connectivity listener
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) auth.removeAuthStateListener(mAuthStateListener);
        circularProgressView.stopAnimation();
        //turn off connectivity listener
        unregisterReceiver(broadcastReceiver);
    }

    public void configureApp(FirebaseUser user) {
        Constants.UID = user.getUid();
        Constants.DATABASE.child("users/" + Constants.UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Constants.USER = dataSnapshot.getValue(HilarityUser.class);
                if (Constants.USER != null) {
                    //if Constants.USER holds data, move into main UI
                    startActivity(new Intent(getBaseContext(), HilarityActivity.class));
                } else {
                    //if Constants.USER holds no data, move to login page
                    startActivity(new Intent(AuthActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
