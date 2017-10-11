package com.udacity.gradle.builditbigger.SignInTutorial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Genres.GenreActivity;
import com.udacity.gradle.builditbigger.Language.LanguageSelectorFragment;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.UserSpecific.ChooseUserNameFragment;

import java.util.Arrays;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;


public class MainActivity extends MaterialIntroActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference databaseReference;
    public static final int RC_SIGN_IN = 1;
    private String mUsername;
    public static final String ANONYMOUS = "anonymous";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mUsername = ANONYMOUS;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    Query query = databaseReference.limitToFirst(1).equalTo(user.getUid()).orderByKey();
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() == 1){
                                startActivity(new Intent(getBaseContext(), GenreActivity.class));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!pref.getBoolean("firstTimeRun", true)) {
            // start the preferences activity
            //startActivity(new Intent(getBaseContext(), GenreActivity.class));
            Log.i("jokes", "not first run");
        } else {
            Log.i("jokes", "first run");
            SharedPreferences.Editor editor = pref.edit();
            // avoid for next run
            editor.putBoolean("firstTimeRun", false);
            if (editor.commit()){
                Log.i("jokes", "firsttimeRun saved");
            }
        }

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.primary)
                .buttonsColor(R.color.accent)
                .title("Test title")
                .description("test description")
                .build());
        addSlide(new LanguageSelectorFragment());
        addSlide(new ChooseUserNameFragment());

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

    private void onSignedInInitialize(String username) {
        mUsername = username;
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
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
        startActivity(new Intent(getBaseContext(), GenreActivity.class));
        Log.i("joke", "onFinished called");
    }
}
