package com.udacity.gradle.builditbigger.signInOnboarding.profilePicture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;

public class ProfilePictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.profile_pic_framelayout,
                        ProfilePictureFragment.newInstance())
                .commit();
    }

}
