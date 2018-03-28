package com.udacity.gradle.builditbigger.NewPost.GifPost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.udacity.gradle.builditbigger.R;
public class NewGifSubmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gif_submission);
        String filepath = getIntent().getStringExtra("filepath");
        String number = getIntent().getStringExtra("number");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.new_gif_post_framelayout, NewGifSubmission.newInstance(filepath,number))
                .commit();
    }
}
