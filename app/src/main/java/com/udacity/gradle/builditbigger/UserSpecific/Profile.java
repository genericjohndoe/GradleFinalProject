package com.udacity.gradle.builditbigger.UserSpecific;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.R;



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
    private DatabaseReference userDatabaseReference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        viewPager = root.findViewById(R.id.profile_view_pager);
        viewPager.setAdapter(new ProfilePagerAdapter(getActivity().getSupportFragmentManager()));
        tabLayout = root.findViewById(R.id.profile_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        mUserNameTextView = root.findViewById(R.id.user_name);
        mProfileImageView = root.findViewById(R.id.profile_imageview);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mFirebaseUser.getUid());
        if (mFirebaseUser != null){
            userDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mFirebaseUser.getUid());
            userDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HilarityUser user = dataSnapshot.getValue(HilarityUser.class);
                    Constants.user = user;
                    mUserNameTextView.setText(user.getUserName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Glide.with(this)
                    .load(mFirebaseUser.getPhotoUrl())
                    .into(mProfileImageView);
        }

        return root;
    }

    private class ProfilePagerAdapter extends FragmentPagerAdapter {
        public ProfilePagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) return new HilarityUserJokes();
            if (position == 1) return new HilarityUserGenres();
            return new HilarityUserLikes();
        }
    }
}
