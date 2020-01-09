package com.udacity.gradle.builditbigger.settings.contentCreatorSettings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;

public class ContentCreatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_creator);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.cc_settings_framelayout, ContentCreatorFragment.getInstance())
                .commit();
    }
}
