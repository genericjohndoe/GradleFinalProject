package com.udacity.gradle.builditbigger.messaging.transcripts;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.constants.Constants;

public class TranscriptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcript);
        String path = getIntent().getStringExtra("path");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.transcript_framelayout, TranscriptFragment.newInstance(Constants.UID, path), "transcript")
                .commit();
    }
}
