package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by joeljohnson on 9/28/17.
 */

public class Profile extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private TextView mUserNameTextView;
    private ImageView mProfileImageView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        viewPager = root.findViewById(R.id.profile_view_pager);
        //viewPager.setAdapter();
        tabLayout = root.findViewById(R.id.profile_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        mUserNameTextView = root.findViewById(R.id.user_name);
        mProfileImageView = root.findViewById(R.id.profile_imageview);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null){
            Log.i("joke", mFirebaseUser.getDisplayName());
            Log.i("joke", mFirebaseUser.getEmail());
            Log.i("joke", mFirebaseUser.getPhotoUrl().toString());
            mUserNameTextView.setText(mFirebaseUser.getDisplayName());
            Glide.with(this)
                    .load(mFirebaseUser.getPhotoUrl())
                    .into(mProfileImageView);
        }

        return root;
    }
}
