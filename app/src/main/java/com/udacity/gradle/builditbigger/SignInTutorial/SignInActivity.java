package com.udacity.gradle.builditbigger.SignInTutorial;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.udacity.gradle.builditbigger.R;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ImageView email = findViewById(R.id.email_sign_in_button);
        email.setOnClickListener(view ->{});
        ImageView gmail = findViewById(R.id.gmail_sign_in_button);
        gmail.setOnClickListener(view->{});
    }

}
