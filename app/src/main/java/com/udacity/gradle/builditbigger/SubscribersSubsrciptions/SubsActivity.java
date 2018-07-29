package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;

public class SubsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subs);
        String uid = getIntent().getStringExtra("uid");
        boolean getFollowers = getIntent().getBooleanExtra("getFollowers", true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.subs_framelayout, SubsFragment.newInstance(uid, getFollowers))
                .commit();
    }
}
