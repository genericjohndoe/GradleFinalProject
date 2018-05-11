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
        //if call from the server side, position have value > -1
        int position = getIntent().getIntExtra("position", -1);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.comment_framelayout, (position == -1) ? CommentFragment.newInstance(uid, pushId) : CommentFragment.newInstance(uid, pushId,position))
                .commit();
    }
}
