package com.udacity.gradle.builditbigger.NewPost.VideoPost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.udacity.gradle.builditbigger.R;

public class VideoPostSubmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_post_submission);
        String filepath = getIntent().getStringExtra("filepath");
        String number = getIntent().getStringExtra("number");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.new_video_post_framelayout, NewVideoSubmission.newInstance(filepath, number))
                .commit();
    }
}
