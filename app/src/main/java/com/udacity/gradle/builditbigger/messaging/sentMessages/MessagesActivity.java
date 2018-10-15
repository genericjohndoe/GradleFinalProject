package com.udacity.gradle.builditbigger.messaging.sentMessages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.R;

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.messaging_framelayout, SentMessagesFragment.newInstance(Constants.UID), "sent messages")
                .commit();
    }
}
