package com.udacity.gradle.builditbigger.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.databinding.FragmentProfileBinding;
import com.udacity.gradle.builditbigger.interfaces.HideFAB;
import com.udacity.gradle.builditbigger.isFollowing.IsFollowingLiveData;
import com.udacity.gradle.builditbigger.models.Collection;
import com.udacity.gradle.builditbigger.newPost.NewPostActivity2;
import com.udacity.gradle.builditbigger.profile.userCollections.HilarityUserCollections;
import com.udacity.gradle.builditbigger.profile.userLikes.HilarityUserLikes;
import com.udacity.gradle.builditbigger.profile.userPosts.HilarityUserJokes;
import com.udacity.gradle.builditbigger.profile.userScheduledPosts.HilarityUserScheduledPosts;
import com.udacity.gradle.builditbigger.settings.userSettings.UserSettingsActivity;
import com.udacity.gradle.builditbigger.subscribersSubsrciptions.SubsActivity;


/**
 * Profile Class responsible for showing user data seen in profile page
 */

public class Profile extends Fragment implements HideFAB {
    private String uid;
    private FragmentProfileBinding binding;
    private boolean isFollowed;

    /**
     * instantiates new instance of Profile Fragment
     * @param uid the unique id for a specific user
     * @return the profile for a specific user
     */
    public static Profile newInstance(String uid){
        Profile profile = new Profile();
        Bundle bundle = new Bundle();
        bundle.putString("uid",uid);
        profile.setArguments(bundle);
        return profile;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        uid = getArguments().getString(getString(R.string.uid));
        //test
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);

        binding.profileViewPager.setAdapter(new ProfilePagerAdapter(getActivity().getSupportFragmentManager()));
        //changes function of floating action menu based on what fragment is shown in the profile viewpager
        binding.profileViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                configureFAB(position);
                FragmentFocusLiveData.getFragmentFocusLiveData().setValue(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        binding.profileTabLayout.setupWithViewPager(binding.profileViewPager);




        binding.subscribersTv.setOnClickListener(view -> createSubsIntent(true));
        binding.subscriptionsTv.setOnClickListener(view -> createSubsIntent(false));

        //originally calls new post dialog, changed when configureFAB is called
        binding.newPostFab.setOnClickListener(view -> showNewPostFragment());
        //object beneath provides data to fragment

        UserInfoViewModel userInfoViewModel = ViewModelProviders.of(this,
                new UserInfoViewModelFactory(uid))
                .get(UserInfoViewModel.class);

        userInfoViewModel.getUserName().observe(this, userName -> {
                getActivity().setTitle(userName);
            }
        );

        userInfoViewModel.getUserProfileImg().observe(this, profileUrl -> {
            Glide.with(Profile.this)
                    .load(profileUrl)
                    .into(binding.profileImageview);
            Glide.with(Profile.this)
                    .load(profileUrl)
                    .into(binding.smallProfileImageview);
            }
        );

        userInfoViewModel.getNumPostLiveData().observe(this, numPosts -> {
                    binding.postsTv.setText(numPosts != null ? numPosts + " " : "0 ");

                }
        );

        userInfoViewModel.getNumFollowingLiveData().observe(this, numFollowing -> {
            binding.subscriptionsTv.setText(numFollowing != null ? numFollowing + " " : "0 ");
        });

        userInfoViewModel.getNumFollowersLiveData().observe(this, numFollowers -> {
            binding.subscribersTv.setText(numFollowers != null ? numFollowers + " " : "0 ");
        });

        userInfoViewModel.getTaglineLiveData().observe(this, tagline ->{
            binding.taglineTextView.setText((tagline != null) ? tagline : Constants.TAGLINE);
        });

        binding.appBarLayout.addOnOffsetChangedListener(((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                binding.smallProfileImageview.setVisibility(View.VISIBLE);
            } else if (verticalOffset == 0) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            } else {
                binding.smallProfileImageview.setVisibility(View.GONE);
            }
        }));

        if (!uid.equals(Constants.UID)) {
            new IsFollowingLiveData(uid).observe(this, isFollowed -> {
                this.isFollowed = isFollowed;
                if (isFollowed) {
                    binding.subscribeButton.setText(R.string.subscribed);
                } else {
                    binding.subscribeButton.setText(R.string.subscribe);
                }
            });
        }
        if (uid.equals(Constants.UID)) binding.subscribeButton.setText(R.string.edit_profile);
        binding.subscribeButton.setOnClickListener(view -> {
            if (uid.equals(Constants.UID)){
                Intent intent = new Intent(getActivity(), UserSettingsActivity.class);
                startActivity(intent);
            } else {
                if (!isFollowed){
                    Constants.DATABASE.child("followers/"+uid+"/list/"+Constants.UID).setValue(Constants.USER);
                } else {
                    Constants.DATABASE.child("followers/"+uid+"/list/"+Constants.UID).removeValue();
                }

            }
        });

        binding.profileTabLayout.getTabAt(0).setCustomView(R.layout.icon_post);
        binding.profileTabLayout.getTabAt(1).setCustomView(R.layout.icon_collections);
        binding.profileTabLayout.getTabAt(2).setCustomView(R.layout.icon_likes);
        if (uid.equals(Constants.UID)) binding.profileTabLayout.getTabAt(3).setCustomView(R.layout.icon_post);
        return binding.getRoot();
    }

    /**
     * dictates the function and look of the FloatingActionMenu object
     * @param state is related to the viewpager page selected
     */
    private void configureFAB(int state) {
        if (state == 0) {
            binding.fam.showMenu(true);
            binding.newPostFab.setOnClickListener(view -> showNewPostFragment());
        } else if (state == 1) {
            binding.fam.showMenu(true);
            binding.newPostFab.setOnClickListener(view -> showNewGenreDialog());
        } else {
            binding.fam.showMenu(true);
            binding.newPostFab.setOnClickListener(null);
        }
    }

    private void createSubsIntent(boolean getFollowers){
        Intent intent = new Intent(getActivity(), SubsActivity.class);
        intent.putExtra(getString(R.string.uid), uid);
        intent.putExtra("getFollowers", getFollowers);
        startActivity(intent);
    }

    /**
     * shows new post dialog from profile when user posts are shown
     */
    //todo allow for post to be added to collection upon creation
    private void showNewPostFragment(){
        Intent intent = new Intent(getActivity(), NewPostActivity2.class);
        intent.putExtra(getString(R.string.number), binding.postsTv.getText().toString().split(" ")[0]);
        startActivity(intent);
    }

    /**
     * shows new genre dialog from profile when user created genres are shown
     */
    private void showNewGenreDialog(){
        new MaterialDialog.Builder(getActivity())
                .customView(R.layout.dialog_new_genre, true)
                .positiveText(R.string.submit)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> {
                        View view = dialog.getCustomView();
                        String genreTitle = ((EditText) view.findViewById(R.id.new_genre_title_et)).getText().toString();
                        boolean isRestricted = ((CheckBox) view.findViewById(R.id.restricted_checkBox)).isChecked();
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("usercollections/" + Constants.UID).push();
                        Collection newGenre = new Collection(genreTitle, Constants.USER.getUserName(), isRestricted,
                                System.currentTimeMillis(), Constants.USER.getUid(), db.getKey());
                        db.setValue(newGenre);
                })
                .onNegative((dialog, which) -> dialog.dismiss())
                .show().setCanceledOnTouchOutside(false);
    }

    /**
     * ProfilePagerAdapter Class used to enable for horizontal scrolling between user posts, collections and liked posts
     */
    private class ProfilePagerAdapter extends FragmentPagerAdapter {
        public ProfilePagerAdapter(FragmentManager fm) {super(fm);}

        Fragment[] fragmentArray = new Fragment[]{HilarityUserJokes.newInstance(uid, Profile.this), HilarityUserCollections.newInstance(uid, Profile.this),
                HilarityUserLikes.newInstance(uid, Profile.this),HilarityUserScheduledPosts.newInstance(Profile.this)};

        String[] tabTitles = new String[]{"Posts", "Genres", "Likes","Scheduled Posts"};

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
             if (uid.equals(Constants.UID)) return 4;
             return 3;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentArray[position];
        }

        public Fragment getSelectedItem(int position) {return fragmentArray[position];}
    }

    /**
     * hides floating action menu
     */
    public void hideFAB() {
        binding.fam.hideMenu(true);
    }

    /**
     * shows floating action menu
     */
    public void showFAB() {
        binding.fam.showMenu(true);
    }

    /**
     * @return a reference to the search FAB in the menu
     */
    public FloatingActionButton getFAB(){
        return binding.searchFab;
    }

    @Override
    public FloatingActionMenu getFAM() {
        binding.fam.close(true);
        return binding.searchFam;
    }
}
