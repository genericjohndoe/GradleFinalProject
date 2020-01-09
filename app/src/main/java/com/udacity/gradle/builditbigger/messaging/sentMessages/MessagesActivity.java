package com.udacity.gradle.builditbigger.messaging.sentMessages;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.constants.Constants;

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
