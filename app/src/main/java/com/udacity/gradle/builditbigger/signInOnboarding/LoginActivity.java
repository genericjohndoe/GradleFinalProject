package com.udacity.gradle.builditbigger.signInOnboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.mainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.models.HilarityUser;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.signInOnboarding.userName.UserNameActivity;

/**
 * A login screen that offers login via email/password and sign in with Google.
 */
public class LoginActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;
    //private int RC_SAVE = 2;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuthStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if (user != null) configureApp(user);
        };

        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = findViewById(R.id.email_editText);

        mPasswordView = findViewById(R.id.password_editText);
        mPasswordView.setOnEditorActionListener((TextView textView, int id, KeyEvent keyEvent) -> {
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
        );

        findViewById(R.id.sign_in_button).setOnClickListener(view -> attemptLogin());

        ImageButton signInButton = findViewById(R.id.google_sign_in_button);

        signInButton.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start listening for changes in authorization state
        auth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop listening for changes in authorization state
        if (mAuthStateListener != null) auth.removeAuthStateListener(mAuthStateListener);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            signIn(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && !email.contains(" ") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 8;
    }

    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                            if (!task.isSuccessful()) {
                                //if user doesn't have account, one  is automatically created
                                createAccount(email, password);
                            }
                        }
                );
    }

    private void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign in Failed", Toast.LENGTH_SHORT).show();
            }
        }
        /*if (requestCode == RC_SAVE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Credentials saved", Toast.LENGTH_SHORT).show();
            } else {
                //Log.e(TAG, "SAVE: Canceled by user", e);
            }
        }*/
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential);
    }

    public void configureApp(FirebaseUser user) {
        Constants.UID = user.getUid();
        Constants.DATABASE.child("users/" + Constants.UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Constants.USER = dataSnapshot.getValue(HilarityUser.class);
                if (Constants.USER != null) {
                    //move to main UI
                    startActivity(new Intent(LoginActivity.this, HilarityActivity.class));
                } else {
                    //move to page to pick user and set birth information
                    startActivity(new Intent(LoginActivity.this, UserNameActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}

