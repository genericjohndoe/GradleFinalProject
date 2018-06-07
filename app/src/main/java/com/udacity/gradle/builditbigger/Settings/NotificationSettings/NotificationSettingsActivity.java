package com.udacity.gradle.builditbigger.Settings.NotificationSettings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Settings.UserSettings.UserSettingsFragment;

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
