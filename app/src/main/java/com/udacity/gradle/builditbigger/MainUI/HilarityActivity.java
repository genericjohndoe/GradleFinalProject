package com.udacity.gradle.builditbigger.MainUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Explore.ExploreFragment;
import com.udacity.gradle.builditbigger.Feed.FeedFragment;
import com.udacity.gradle.builditbigger.Forums.Questions.ForumFragment;
import com.udacity.gradle.builditbigger.Messaging.SentMessages.MessagesActivity;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Settings.SettingsFragment;
import com.udacity.gradle.builditbigger.SignInTutorial.LoginActivity;

/**
 * shows pertinent information to the user
 */
public class HilarityActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //todo replace imgs with gifs in left nav
    DrawerLayout drawer;
    int fragmentNumber;
    String otherUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hilarity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentNumber = getIntent().getIntExtra("number", 0);
        otherUid = getIntent().getStringExtra("uid");
        Fragment fragment;
        /*if (savedInstanceState !=null) {
            fragmentNumber = savedInstanceState.getInt("number", 0);
            otherUid = savedInstanceState.getString("uid");
        }*/
        switch(fragmentNumber){
            case 1:
                fragment = Profile.newInstance(Constants.UID);
                break;
            case 2:
                fragment = FeedFragment.newInstance(Constants.UID);
                setTitle("Feed");
                break;
            case 3:
                fragment = ExploreFragment.newInstance(Constants.UID);
                setTitle("Explore");
                break;
            case 4:
                fragment = Profile.newInstance(otherUid);
                break;
            case 5:
                fragment = ForumFragment.newInstance();
                setTitle("Forums");
                break;
            case 6:
                fragment = SettingsFragment.newInstance();
                setTitle("Settings");
                break;
            default:
                fragment = Profile.newInstance(Constants.UID);
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.hilarity_content_frame, fragment, "profile")
                .commit();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("number", fragmentNumber);
        outState.putString("uid", otherUid);
        super.onSaveInstanceState(outState);
    }*/

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hilarity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_message){
            startActivity(new Intent(this, MessagesActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent(this, HilarityActivity.class);
        if (id == R.id.profile_page) {
            intent.putExtra("number", 1);
        } else if (id == R.id.feed_page) {
            intent.putExtra("number", 2);
        } else if (id == R.id.explore_page) {
            intent.putExtra("number", 3);
        } else if (id == R.id.forums_page) {
            intent.putExtra("number", 5);
        } else if (id == R.id.settings_page) {
            intent.putExtra("number", 6);
        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        if (id != R.id.logout){
            startActivity(intent);
            finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


