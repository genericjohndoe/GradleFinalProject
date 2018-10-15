package com.udacity.gradle.builditbigger.messaging.composeMessage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.R;

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
