package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.R;

public class SubsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subs);
        int fragmenttype = getIntent().getIntExtra("fragment", 0);
        String uid = getIntent().getStringExtra("uid");
        Fragment fragment = new Fragment();
        switch (fragmenttype){
            case 1:
                fragment = SubscribersFragment.newInstance(uid);
                break;
            case 2:
                fragment = SubscriptionsFragment.newInstance(uid);
                break;
            default:
                startActivity(new Intent(this, HilarityActivity.class));
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.subs_framelayout, fragment)
                .commit();
    }
}
