package com.udacity.gradle.builditbigger.post;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;

/**
 * DEPRECATED
 */

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        String title = getIntent().getStringExtra(getString(R.string.title));
        String body = getIntent().getStringExtra(getString(R.string.body));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.post_fragment, PostFragment.newInstance(title, body, this))
                .commit();
    }
}
