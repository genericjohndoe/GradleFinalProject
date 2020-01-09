package com.udacity.gradle.builditbigger.messaging.composeMessage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.constants.Constants;

public class ComposeMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.compose_message_framelayout, ComposeMessageFragment.newInstance(Constants.UID), "transcript")
                .commit();
    }
}
