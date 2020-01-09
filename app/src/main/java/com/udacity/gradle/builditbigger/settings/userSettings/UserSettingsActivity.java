package com.udacity.gradle.builditbigger.settings.userSettings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;

public class UserSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.user_settings_settings_framelayout, new UserSettingsFragment())
                .commit();
    }

}
