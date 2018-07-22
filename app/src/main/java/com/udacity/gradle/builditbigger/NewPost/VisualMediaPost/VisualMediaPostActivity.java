package com.udacity.gradle.builditbigger.NewPost.VisualMediaPost;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;
public class VisualMediaPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_media_post);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.visual_media_post_framelayout,
                        VisualMediaPostFragment.newInstance("2"))
                .commit();
    }
}
