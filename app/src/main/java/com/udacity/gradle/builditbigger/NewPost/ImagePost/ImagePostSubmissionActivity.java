package com.udacity.gradle.builditbigger.NewPost.ImagePost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.udacity.gradle.builditbigger.R;

public class ImagePostSubmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post_submission);
        String filepath = getIntent().getStringExtra("filepath");
        String number = getIntent().getStringExtra("number");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.new_image_post_framelayout, NewImageSubmission.newInstance(filepath, number))
                .commit();
    }
}
