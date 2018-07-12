package com.udacity.gradle.builditbigger.SignInTutorial.ProfilePicture;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.gradle.builditbigger.Camera.LifeCycleCamera;
import com.udacity.gradle.builditbigger.NewPost.VisualMediaPost.VisualMediaPostFragment;
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
