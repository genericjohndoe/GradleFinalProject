package com.udacity.gradle.builditbigger.Profile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Interfaces.HideFAB;
import com.udacity.gradle.builditbigger.Interfaces.VideoInfoTransfer;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.Collection;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.Models.VideoInfo;
import com.udacity.gradle.builditbigger.NewPost.NewPostActivity2;
import com.udacity.gradle.builditbigger.Profile.UserCollections.HilarityUserCollections;
import com.udacity.gradle.builditbigger.Profile.UserLikes.HilarityUserLikes;
import com.udacity.gradle.builditbigger.Profile.UserPosts.HilarityUserJokes;
import com.udacity.gradle.builditbigger.Profile.UserPosts.OrientationControlViewModel;
import com.udacity.gradle.builditbigger.Profile.UserPosts.OrientationControlViewModelFactory;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SubscribersSubsrciptions.SubsActivity;
import com.udacity.gradle.builditbigger.VideoLifeCyclerObserver;
import com.udacity.gradle.builditbigger.databinding.FragmentProfileBinding;
import com.udacity.gradle.builditbigger.isFollowing.IsFollowingLiveData;


/**
 * Profile Class responsible for showing user data seen in profile page
 */

public class Profile extends Fragment implements HideFAB {
    //todo populate UI with info from database
    //todo find out why viewpager fragments don't immediately show when profile page is reloaded
    //todo convert svg to text use in viewpager
    private String uid;
    private FragmentProfileBinding binding;
    private OrientationControlViewModel orientationControlViewModel;
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
        uid = getArguments().getString("uid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding.profileViewPager.setAdapter(new ProfilePagerAdapter(getActivity().getSupportFragmentManager()));
        //changes function of floating action menu based on what fragment is shown in the profile viewpager
        binding.profileViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                configureFAB(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        binding.profileTabLayout.setupWithViewPager(binding.profileViewPager);

        binding.subscribersTv.setOnClickListener(view -> createSubsIntent(1));

        binding.subscriptionsTv.setOnClickListener(view -> createSubsIntent(2));

        //originally calls new post dialog, changed when configureFAB is called
        binding.newPostFab.setOnClickListener(view -> showNewPostFragment());
        //object beneath provides data to fragment
        getLifecycle().addObserver(new VideoLifeCyclerObserver(getActivity(), binding.videoPlayer));
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
            }
        );
        //todo replace text with image text
        userInfoViewModel.getNumPostLiveData().observe(this, numPosts -> {
            binding.postsTv.setText(numPosts != null ? numPosts + " posts" : "0 posts");
            }
        );

        userInfoViewModel.getNumFollowingLiveData().observe(this, numFollowing -> {
            binding.subscriptionsTv.setText(numFollowing != null ? numFollowing + " subscriptions" : "0 subscriptions");
        });

        userInfoViewModel.getNumFollowersLiveData().observe(this, numFollowers -> {
            binding.subscribersTv.setText(numFollowers != null ? numFollowers + " subscribers" : "0 subscribers");
        });

        orientationControlViewModel = ViewModelProviders.of(this, new OrientationControlViewModelFactory()).get(OrientationControlViewModel.class);
        orientationControlViewModel.getVideoLiveData().observe(this, videoInfo -> {
            setOrientation(videoInfo);
        });

        orientationControlViewModel.getOrientationLiveData().observe(this, isLandscape ->{
            if (!isLandscape){
                long time = binding.videoPlayer.getPlayer().getCurrentPosition();
                binding.videoPlayer.getPlayer().stop();
                binding.videoPlayer.getPlayer().release();
                binding.coordinatorLayout.setVisibility(View.VISIBLE);
                binding.videoPlayer.setVisibility(View.GONE);
                orientationControlViewModel.getVideoLiveData().setValue(new VideoInfo(null,time));
            }
        });

        binding.appBarLayout.addOnOffsetChangedListener(((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            } else if (verticalOffset == 0) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            }
        }));
        if (!uid.equals(Constants.UID)) {
            new IsFollowingLiveData(uid).observe(this, isFollowed -> {
                this.isFollowed = isFollowed;
                if (isFollowed) {
                    binding.subscribeButton.setText("Subscribed");
                } else {
                    binding.subscribeButton.setText("Subscribe");
                }
            });
        }
        if (uid.equals(Constants.UID)) binding.subscribeButton.setText("Edit Profile");
        binding.subscribeButton.setOnClickListener(view ->{
            if (uid.equals(Constants.UID)){
                //todo create intent to edit profile
            } else {
                if (!isFollowed){
                    Constants.DATABASE.child("followers/"+uid+"/list/"+Constants.UID).setValue(Constants.USER);
                } else {
                    Constants.DATABASE.child("followers/"+uid+"/list/"+Constants.UID).removeValue();
                }

            }
        });
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

    private void createSubsIntent(int fragmenttype){
        Intent intent = new Intent(getActivity(), SubsActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("fragment", fragmenttype);
        getActivity().startActivity(intent);
    }

    /**
     * shows new post dialog from profile when user posts are shown
     */
    //todo replace with method that generates FragmentTransaction
    //todo allow for post to be added to collection upon creation
    private void showNewPostFragment() {
        Intent intent = new Intent(getActivity(), NewPostActivity2.class);
        intent.putExtra("number", binding.postsTv.getText().toString().split(" ")[0]);
        startActivity(intent);
    }

    /**
     * shows new genre dialog from profile when user created genres are shown
     */
    //todo replace with method that generates FragmentTransaction
    //todo allow privacy? if yes, remember to do on server side too
    private void showNewGenreDialog() {
        new MaterialDialog.Builder(getActivity())
                .customView(R.layout.dialog_new_genre, true)
                .positiveText("Submit")
                .negativeText("Cancel")
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
                HilarityUserLikes.newInstance(uid, Profile.this)};

        String[] tabTitles = new String[]{"Posts", "Genres", "Likes"};

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return fragmentArray.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentArray[position];
        }
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

    private void setOrientation(VideoInfo videoInfo){
        Log.i("numMovies", "SetOrienation called");
        if (videoInfo == null){
            binding.videoPlayer.setVisibility(View.GONE);
            binding.coordinatorLayout.setVisibility(View.VISIBLE);
            Log.i("numMovies", "SetOrienation numVideos is 0");
        } else {
            int orientation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
            if (orientation == Surface.ROTATION_0 || orientation == Surface.ROTATION_180) {
                binding.videoPlayer.setVisibility(View.GONE);
                binding.coordinatorLayout.setVisibility(View.VISIBLE);
                long time = binding.videoPlayer.getPlayer().getCurrentPosition();
                binding.videoPlayer.getPlayer().setPlayWhenReady(false);
                orientationControlViewModel.getVideoLiveData().setValue(new VideoInfo(videoInfo.getUrl(),time));
                Log.i("numMovies", "SetOrienation rot is 0 or 180");
            } else {
                binding.videoPlayer.setVisibility(View.VISIBLE);
                binding.coordinatorLayout.setVisibility(View.GONE);
                Log.i("numMovies", "SetOrienation rot is 90 or 270");
                setVideoPlayer(videoInfo);
            }
        }
    }

    private void setVideoPlayer(VideoInfo videoInfo){
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), "Hilarity"), bandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        if (binding.videoPlayer.getPlayer() != null) {
            binding.videoPlayer.getPlayer().prepare(new ExtractorMediaSource(Uri.parse(videoInfo.getUrl()),
                    dataSourceFactory, extractorsFactory, null, null), false, false);
            binding.videoPlayer.getPlayer().seekTo(videoInfo.getTimeElapsed());
            binding.videoPlayer.getPlayer().setPlayWhenReady(true);
        }
    }
}
