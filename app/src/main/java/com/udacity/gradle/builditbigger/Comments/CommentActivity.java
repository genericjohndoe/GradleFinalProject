package com.udacity.gradle.builditbigger.Comments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.udacity.gradle.builditbigger.R;
public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        String uid = getIntent().getStringExtra("uid");
        String pushId = getIntent().getStringExtra("pushId");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.comment_framelayout, CommentFragment.newInstance(uid, pushId))
                .commit();
    }
}
