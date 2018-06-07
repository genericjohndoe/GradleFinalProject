package com.udacity.gradle.builditbigger.Settings.UserSettings;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.udacity.gradle.builditbigger.R;

public class UserSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.user_settings_settings_framelayout, UserSettingsFragment.newInstance())
                .commit();
    }

}
