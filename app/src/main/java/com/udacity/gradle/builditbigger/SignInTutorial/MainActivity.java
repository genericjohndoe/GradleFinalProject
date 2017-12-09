package com.udacity.gradle.builditbigger.SignInTutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.UserSpecific.ChooseUserNameFragment;
import com.udacity.gradle.builditbigger.UserSpecific.HilarityUser;

import java.util.Arrays;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;


public class MainActivity extends MaterialIntroActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    public static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*todo change intro screen to show legal stuff, prompts new user to pick user name
        runs query on database when user stops typing, okay'd by animation
        show introduction page then load app activity
        do test to see where Constants.UID gets initialize
        todo*/
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) { // User is signed in
                    configureApp(user);
                } else { //user isn't signed in, prompts user to sign in
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);

                    addSlide(new SlideFragmentBuilder()
                            .backgroundColor(R.color.primary)
                            .buttonsColor(R.color.accent)
                            .title("Test title")
                            .description("test description")
                            .build());
                    addSlide(new ChooseUserNameFragment());
                }
            }
        };

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (user != null) configureApp(user);
        Log.i("uid", Constants.UID);
        Log.i("Hilarity", "onFinished called");
    }

    public void configureApp(FirebaseUser user) {
        Constants.UID = user.getUid();
        Constants.DATABASE.child("users/" + Constants.UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Constants.USER = dataSnapshot.getValue(HilarityUser.class);
                if (Constants.USER != null)
                    startActivity(new Intent(getBaseContext(), HilarityActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
