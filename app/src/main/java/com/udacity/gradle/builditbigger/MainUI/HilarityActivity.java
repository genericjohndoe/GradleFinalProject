package com.udacity.gradle.builditbigger.MainUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
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
import android.view.Surface;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Explore.ExploreFragment;
import com.udacity.gradle.builditbigger.Feed.FeedFragment;
import com.udacity.gradle.builditbigger.Forums.Questions.ForumFragment;
import com.udacity.gradle.builditbigger.Messaging.SentMessages.MessagesActivity;
import com.udacity.gradle.builditbigger.Models.VideoInfo;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.Profile.UserPosts.OrientationControlViewModel;
import com.udacity.gradle.builditbigger.Profile.UserPosts.OrientationControlViewModelFactory;
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
    OrientationControlViewModel orientationControlViewModel;
    FullScreenVideoDialog dialog;
    boolean isVideoPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hilarity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        createNotificationChannel();
        messagingToken();
        setSupportActionBar(toolbar);
        fragmentNumber = getIntent().getIntExtra("number", 0);
        otherUid = getIntent().getStringExtra("uid");
        Fragment fragment;
        String tag;
        switch (fragmentNumber) {
            case 1:
                fragment = Profile.newInstance(Constants.UID);
                tag = "profile";
                break;
            case 2:
                fragment = FeedFragment.newInstance(Constants.UID);
                setTitle("Feed");
                tag = "feed";
                break;
            case 3:
                fragment = ExploreFragment.newInstance(Constants.UID);
                setTitle("Explore");
                tag = "explore";
                break;
            case 4:
                fragment = Profile.newInstance(otherUid);
                tag = "other profile";
                break;
            case 5:
                fragment = ForumFragment.newInstance();
                setTitle("Forums");
                tag = "forums";
                break;
            case 6:
                fragment = SettingsFragment.newInstance();
                setTitle("Settings");
                tag = "settings";
                break;
            default:
                fragment = Profile.newInstance(Constants.UID);
                tag = "profile";
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.hilarity_content_frame, fragment, tag)
                .commit();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.green_8));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Fragment fragment2 = getSupportFragmentManager().findFragmentByTag("full screen video");
        if(fragment2 != null) getSupportFragmentManager().beginTransaction().remove(fragment2).commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        orientationControlViewModel = ViewModelProviders.of(this, new OrientationControlViewModelFactory()).get(OrientationControlViewModel.class);
        orientationControlViewModel.getVideoPlayingMutableLiveData().observe(this, videoPlaying -> {
            Log.i("orientation3", videoPlaying + " = hilarity activity is video playing?");
            isVideoPlaying = videoPlaying;
            formatUiForVideo(videoPlaying);
        });

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
        if (id == R.id.action_message) {
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
        if (id != R.id.logout) {
            startActivity(intent);
            finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        Log.i("orientation3", "HilarityActivity, onConfigurationChanged called");
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (dialog != null && dialog.isVisible()) {
                //dialog.pause();
                orientationControlViewModel.getVideoLiveData().setValue(new VideoInfo(dialog.getUrl(), dialog.getPosition()));
                dialog.dismiss();
                Fragment fragment2 = getSupportFragmentManager().findFragmentByTag("full screen video");
                if (fragment2 != null) getSupportFragmentManager().beginTransaction().remove(fragment2).commit();
            }
            orientationControlViewModel.getOrientationLiveData().setValue(false);
            Log.i("orientation3", "HilarityActivity, orientation is in portrait, set to false");
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientationControlViewModel.getOrientationLiveData().setValue(true);
            if (dialog != null && !dialog.isVisible()) dialog.show(getSupportFragmentManager(), "full screen video");
            Log.i("orientation3", "HilarityActivity, orientation is in landscape, set to true");
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Messages channel";
            String description = "Channel for incoming direct messages";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("new_message_channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void messagingToken() {
        Constants.DATABASE.child("messagingtokens/" + Constants.UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Constants.DATABASE.child("messagingtokens/" + Constants.UID).setValue(FirebaseInstanceId.getInstance().getToken());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void formatUiForVideo(boolean isVideoPlaying){
        if (!isVideoPlaying) {
            Log.i("orientation3", "HA formatUiForVideos no videos playing, locked in portrait");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            Log.i("orientation3", "HA formatUiForVideos video is playing, orientation unspecified");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                orientationControlViewModel.getVideoLiveData().observe(this, videoInfo -> {
                    Log.i("orientation3", "HA formatUiForVideos video info passed to dialog");
                    setUpDialog(videoInfo);
                });
        }

    }

    public void setUpDialog(VideoInfo videoInfo){
        Log.i("orientation3", "HA setUpDialog dialog set up");
        dialog = FullScreenVideoDialog.getInstance(videoInfo.getUrl(), videoInfo.getTimeElapsed());
    }

}


