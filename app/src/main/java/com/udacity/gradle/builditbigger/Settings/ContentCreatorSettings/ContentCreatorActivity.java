package com.udacity.gradle.builditbigger.Settings.ContentCreatorSettings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Settings.NotificationSettings.NotificationSettingsFragment;

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
