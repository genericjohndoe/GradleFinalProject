package com.udacity.gradle.builditbigger.MainUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Explore.ExploreFragment;
import com.udacity.gradle.builditbigger.Feed.FeedFragment;
import com.udacity.gradle.builditbigger.Messaging.SentMessages.SentMessagesFragment;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SignInTutorial.LoginActivity;

/**
 * shows pertinent information to the user
 */
public class HilarityActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //todo replace imgs with gifs in left nav
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Hoe8", "activity created");
        setContentView(R.layout.activity_hilarity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.hilarity_content_frame, Profile.newInstance(Constants.UID), "profile")
                .commit();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_message){
            Constants.changeFragment(R.id.hilarity_content_frame, SentMessagesFragment.newInstance(Constants.UID), this);
            //changeFragment(new MessagesFragment(), "messages");
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile_page) {
            changeFragment(Profile.newInstance(Constants.UID), "profile");
            //this.setTitle("Profile");
        } else if (id == R.id.feed_page) {
            changeFragment(FeedFragment.newInstance(Constants.UID), "feed");
            setTitle("Feed");
        } else if (id == R.id.explore_page) {
            changeFragment(ExploreFragment.newInstance(Constants.UID), "explore");
            setTitle("Explore");
        }  else if (id == R.id.forums_page) {

        }  else if (id == R.id.settings_page) {

        } else if (id == R.id.logout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) startActivity(new Intent(this, LoginActivity.class));
                        }
                    );
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.hilarity_content_frame, fragment, tag)
                .addToBackStack(null)
                .commit();
    }


}


