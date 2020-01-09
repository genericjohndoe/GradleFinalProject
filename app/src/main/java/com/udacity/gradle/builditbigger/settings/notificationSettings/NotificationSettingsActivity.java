package com.udacity.gradle.builditbigger.settings.notificationSettings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;

public class NotificationSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.notification_settings_framelayout, NotificationSettingsFragment.newInstance())
                .commit();
    }
}
