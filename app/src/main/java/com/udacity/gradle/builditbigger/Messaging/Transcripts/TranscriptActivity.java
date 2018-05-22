package com.udacity.gradle.builditbigger.Messaging.Transcripts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Messaging.Transcripts.TranscriptFragment;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;

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
