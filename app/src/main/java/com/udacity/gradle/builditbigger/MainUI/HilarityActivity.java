package com.udacity.gradle.builditbigger.MainUI;

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

import com.udacity.gradle.builditbigger.R;

public class HilarityActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //todo replace imgs with gifs in left nav
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hilarity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.hilarity_content_frame, new Profile(), "profile")
                .commit();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        Drawer result = new DrawerBuilder()
//                .withActivity(this)
//                .withToolbar(toolbar)
//                .withDisplayBelowStatusBar(true)
//                .withGenerateMiniDrawer(true)
//                .addDrawerItems(
//                        new PrimaryDrawerItem().withIcon(R.drawable.ic_profile_icon),
//                        new PrimaryDrawerItem().withIcon(R.drawable.ic_feed_image),
//                        new PrimaryDrawerItem().withIcon(R.drawable.ic_explore_image),
//                        new PrimaryDrawerItem().withIcon(R.drawable.ic_meme_creator),
//                        new PrimaryDrawerItem().withIcon(R.drawable.ic_live_stream),
//                        new PrimaryDrawerItem().withIcon(R.drawable.ic_payment),
//                        new PrimaryDrawerItem().withIcon(R.drawable.ic_settings),
//                        new PrimaryDrawerItem().withIcon(R.drawable.ic_logout)
//                )
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        // do something with the clicked item :D
//                        return true;
//                    }
//                })
//                .buildView();
//
//        new MiniDrawer().withDrawer(result).build(this);
//
//        CoordinatorLayout cl = findViewById(R.id.drawer_layout);
//        cl.addView(new MiniDrawer().withDrawer(result).build(this));


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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile_page) {
            changeFragment(new Profile(), "profile");
            //this.setTitle("Profile");
        } else if (id == R.id.feed_page) {
            changeFragment(new FeedFragment(), "feed");
            setTitle("Feed");
        } else if (id == R.id.explore_page) {
            changeFragment(new ExploreFragment(), "explore");
            setTitle("Explore");
        } else if (id == R.id.meme_creator_page) {

        } else if (id == R.id.live_stream_page) {

        } else if (id == R.id.forums_page) {

        } else if (id == R.id.paypal_page) {

        } else if (id == R.id.settings_page) {

        } else if (id == R.id.logout) {

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
/*Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withGenerateMiniDrawer(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIcon(R.drawable.ic_profile_icon),
                        new PrimaryDrawerItem().withIcon(R.drawable.ic_feed_image),
                        new PrimaryDrawerItem().withIcon(R.drawable.ic_explore_image),
                        new PrimaryDrawerItem().withIcon(R.drawable.ic_meme_creator),
                        new PrimaryDrawerItem().withIcon(R.drawable.ic_live_stream),
                        new PrimaryDrawerItem().withIcon(R.drawable.ic_payment),
                        new PrimaryDrawerItem().withIcon(R.drawable.ic_settings),
                        new PrimaryDrawerItem().withIcon(R.drawable.ic_logout)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        return true;
                    }
                })
                .buildView();

        new MiniDrawer().withDrawer(result);*/

