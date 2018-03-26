package com.udacity.gradle.builditbigger.SignInTutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Models.HilarityUser;

import java.util.Arrays;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

/**
 * sign in and on boarding
 */
public class MainActivity extends MaterialIntroActivity {

    public static final String HILARITY  = "Hilarity";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    public static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Constants.FIREBASEDATABASE = FirebaseDatabase.getInstance();
        //todo find why app fails when closed out from profile page and try to reopen
        Constants.FIREBASEDATABASE.setPersistenceEnabled(true);
        Constants.DATABASE = Constants.FIREBASEDATABASE.getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = firebaseAuth -> {
                user = firebaseAuth.getCurrentUser();
                if (user != null) { // User is signed in
                    configureApp(user);
                    Log.i(HILARITY, "user");
                } else { //user isn't signed in, prompts user to sign in
                    Log.i(HILARITY, "no user");
                    /*startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList( new AuthUI.IdpConfig.EmailBuilder().build(),
                                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);*/

                    Intent intent  = AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setTheme(R.style.GreenTheme)
                            .setAvailableProviders(
                                    Arrays.asList( new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                            .build();
                    try {
                        //startActivityForResult(intent, RC_SIGN_IN);
                    } catch (Throwable e){
                        Log.i(HILARITY, e.toString());
                        Log.i(HILARITY, e.getMessage());
                        Log.i(HILARITY, e.getCause().toString());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Constants.DATABASE.child("userlist/" + user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            configureApp(user);
                        } else {
                            addSlide(new SlideFragmentBuilder()
                                    .backgroundColor(R.color.primary)
                                    .buttonsColor(R.color.accent)
                                    .title("Test title")
                                    .description("test description")
                                    .build());
                            addSlide(new ChooseUserNameFragment());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            } else {
                Log.i("login failed", "");
            }
        }
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
        //Constants.FIREBASEDATABASE.setPersistenceEnabled(true);
        Constants.UID = user.getUid();
        Constants.DATABASE.child("users/" + Constants.UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Constants.USER = dataSnapshot.getValue(HilarityUser.class);
                if (Constants.USER != null) startActivity(new Intent(getBaseContext(), HilarityActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
