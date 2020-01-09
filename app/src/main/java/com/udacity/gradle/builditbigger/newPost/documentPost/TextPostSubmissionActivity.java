package com.udacity.gradle.builditbigger.newPost.documentPost;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.models.Post;

public class TextPostSubmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_post_submission);
        String title = getIntent().getStringExtra("title");
        String body = getIntent().getStringExtra("body");
        String tagline = getIntent().getStringExtra("tagline");
        String number = getIntent().getStringExtra("number");
        String synopsis = getIntent().getStringExtra("synopsis");
        Post post = getIntent().getParcelableExtra("post");
        Fragment fragment = (post == null) ? NewTextPostSubmissionFragment.newInstance(title,body,tagline,number, synopsis) :
                NewTextPostSubmissionFragment.newInstance(post);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.new_text_submission_framelayout, fragment)
                .commit();
    }
}
