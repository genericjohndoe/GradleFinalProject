package com.udacity.gradle.builditbigger.NewPost;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.NewPost.GifPost.NewGifPost;
import com.udacity.gradle.builditbigger.NewPost.ImagePost.NewImagePost;
import com.udacity.gradle.builditbigger.NewPost.TextPost.NewTextPostEditFragment;
import com.udacity.gradle.builditbigger.NewPost.VideoPost.NewVideoPost;
import com.udacity.gradle.builditbigger.R;

public class NewPostActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(GravityCompat.START);
        int posttype = getIntent().getIntExtra("posttype", 1);
        number = getIntent().getStringExtra("number");
        Fragment fragment;
        Log.i("TAGGGG", ""+posttype);
        switch (posttype){
            case 1:
                fragment = NewTextPostEditFragment.newInstance(number);
                break;
            case 2:
                fragment = NewImagePost.newInstance(number);
                break;
            case 3:
                fragment = NewVideoPost.newInstance(number);
                break;
            case 4:
                fragment = NewGifPost.newInstance(number);
                break;
            default:
                fragment = null;

        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.new_post_framelayout2, fragment)
                .commit();

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
        startActivity(new Intent(this, HilarityActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_post_activity2, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent(this, NewPostActivity2.class);
        intent.putExtra("number", number);
        if (id == R.id.nav_text) {
            intent.putExtra("posttype",1);
        } else if (id == R.id.nav_image) {
            intent.putExtra("posttype",2);
        } else if (id == R.id.nav_video) {
            intent.putExtra("posttype",3);
        } else if (id == R.id.nav_gif) {
            intent.putExtra("posttype",4);
        }
        drawer.closeDrawer(GravityCompat.START);
        startActivity(intent);
        return true;
    }


}
