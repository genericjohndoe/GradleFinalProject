package com.udacity.gradle.builditbigger.comments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;
public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        String uid = getIntent().getStringExtra(getString(R.string.uid));
        String pushId = getIntent().getStringExtra(getString(R.string.pushId));
        //if call from the server side, position have value > -1
        int position = getIntent().getIntExtra(getString(R.string.position), -1);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.comment_framelayout, (position == -1) ? CommentFragment.newInstance(uid, pushId) : CommentFragment.newInstance(uid, pushId,position))
                .commit();
    }
}
