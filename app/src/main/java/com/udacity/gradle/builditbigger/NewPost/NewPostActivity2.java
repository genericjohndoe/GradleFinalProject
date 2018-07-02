package com.udacity.gradle.builditbigger.NewPost;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import android.view.animation.DecelerateInterpolator;

import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.NewPost.AudioMediaPost.AudioMediaPostFragment;
import com.udacity.gradle.builditbigger.NewPost.GifPost.NewGifPost;
import com.udacity.gradle.builditbigger.NewPost.ImagePost.NewImagePost;
import com.udacity.gradle.builditbigger.NewPost.TextPost.NewTextPostEditFragment;
import com.udacity.gradle.builditbigger.NewPost.VideoPost.NewVideoPost;
import com.udacity.gradle.builditbigger.NewPost.VisualMediaPost.VisualMediaPostFragment;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.Profile.UserCollections.HilarityUserCollections;
import com.udacity.gradle.builditbigger.Profile.UserLikes.HilarityUserLikes;
import com.udacity.gradle.builditbigger.Profile.UserPosts.HilarityUserJokes;
import com.udacity.gradle.builditbigger.R;

public class NewPostActivity2 extends AppCompatActivity {
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_new_post2);

        int posttype = getIntent().getIntExtra("posttype", 0);
        number = getIntent().getStringExtra("number");

        Intent intent = new Intent(this, NewPostActivity2.class);
        intent.putExtra("number", number);

        Post post = getIntent().getParcelableExtra("post");
        Log.i("iefioejwfw", "post is null: " + (post == null));

        TabLayout tabLayout = findViewById(R.id.tabLayout);

        tabLayout.getTabAt(0).setCustomView(R.layout.icon_document_post);
        tabLayout.getTabAt(1).setCustomView(R.layout.icon_visual_media_post);
        tabLayout.getTabAt(2).setCustomView(R.layout.icon_audio_media_post);

        Fragment fragment;
        switch (posttype){
            case 0:
                fragment = NewTextPostEditFragment.newInstance(number);
                break;
            case 1:
                fragment = VisualMediaPostFragment.newInstance(number);
                break;
            case 2:
                fragment = AudioMediaPostFragment.newInstance(number);
                break;
            case 3:
                fragment = NewTextPostEditFragment.newInstance(post);
                posttype = 0;
                Log.i("iefioejwfw", "posttype is 3");
                break;
            default:
                fragment = null;
        }

        tabLayout.getTabAt(posttype).select();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.new_post_framelayout2, fragment)
                .commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                intent.putExtra("posttype", tab.getPosition());
                startActivity(intent);
                finish();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
