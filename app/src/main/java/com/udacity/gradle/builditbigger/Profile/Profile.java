package com.udacity.gradle.builditbigger.Profile;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Dialog.NewPostDialog;
import com.udacity.gradle.builditbigger.Models.Genre;
import com.udacity.gradle.builditbigger.Interfaces.HideFAB;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Profile.UserGenres.HilarityUserGenres;
import com.udacity.gradle.builditbigger.Profile.UserPosts.HilarityUserJokes;
import com.udacity.gradle.builditbigger.Profile.UserLikes.HilarityUserLikes;
import com.udacity.gradle.builditbigger.SubscribersSubsrciptions.SubscribersFragment;
import com.udacity.gradle.builditbigger.SubscribersSubsrciptions.SubscriptionsFragment;
import com.udacity.gradle.builditbigger.databinding.FragmentProfileBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by joeljohnson on 9/28/17.
 */

public class Profile extends Fragment implements HideFAB {
    //todo populate UI with info from database
    //todo find out why viewpager fragments don't immediately show when profile page is reloaded
    //todo convert svg to text use in viewpager
    private List<String> languages = new ArrayList<>();
    private String uid;
    private FragmentProfileBinding binding;

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

        binding.profileViewPager.setAdapter(new ProfilePagerAdapter(getActivity().getSupportFragmentManager()));
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

        //binding.subscribersTv.setOnClickListener(view -> SubscribersFragment.newInstance(uid));

        //binding.subscriptionsTv.setOnClickListener(view -> changeFragment(new SubscriptionsFragment()));

        //binding.searchFab.setOnClickListener(view ->  showSearchDialog());

        binding.newPostFab.setOnClickListener(view -> showNewJokeDialog());

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

        userInfoViewModel.getNumPostLiveData().observe(this, numPosts -> {
            binding.postsTv.setText(numPosts + " posts");
            }
        );

        userInfoViewModel.getNumFollowingLiveData().observe(this, numFollowing -> {
            binding.subscriptionsTv.setText(numFollowing + " subscriptions");
        });

        userInfoViewModel.getNumFollowersLiveData().observe(this, numFollowers -> {
            binding.subscribersTv.setText(numFollowers + " subscribers");
        });

        userInfoViewModel.getLanguagesLiveData().observe(this, lang -> {
            languages.add(lang);
        });

        return binding.getRoot();
    }

    /**
     * dictates the function and look of the FloatingActionMenu object
     * @param state is the viewpager page selected
     */
    private void configureFAB(int state) {
        if (state == 0) {
            binding.fam.showMenu(true);
            binding.newPostFab.setOnClickListener(view -> showNewJokeDialog());
        } else if (state == 1) {
            binding.fam.showMenu(true);
            binding.newPostFab.setOnClickListener(view -> showNewGenreDialog());
        } else {
            binding.fam.showMenu(true);
            binding.newPostFab.setOnClickListener(null);
        }
    }

    /**
     * shows new post dialog from profile when user posts are shown
     */
    private void showNewJokeDialog() {
        new NewPostDialog().show(getActivity().getSupportFragmentManager(), "dialog");
    }

    /**
     * shows new genre dialog from profile when user created genres are shown
     */
    private void showNewGenreDialog() {
        new MaterialDialog.Builder(getActivity())
                .customView(R.layout.dialog_new_genre, true)
                .positiveText("Submit")
                .negativeText("Cancel")
                .onPositive((dialog, which) -> {
                        View view = dialog.getCustomView();
                        String genreTitle = ((EditText) view.findViewById(R.id.new_genre_title_et)).getText().toString();
                        boolean isRestricted = ((CheckBox) view.findViewById(R.id.restricted_checkBox)).isChecked();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_dropdown_item_1line, languages);
                        AutoCompleteTextView genreLanguage = view.findViewById(R.id.languageAutoCompleteTextView);
                        genreLanguage.setAdapter(adapter);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(System.currentTimeMillis());
                        String formattedDate = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("usergenres/" + Constants.UID).push();
                        Genre newGenre = new Genre(genreTitle, Constants.USER.getUserName(), isRestricted, genreLanguage.toString(),
                                formattedDate, Constants.USER.getUID(), db.getKey());
                        db.setValue(newGenre);
                })
                .onNegative((dialog, which) -> dialog.dismiss())
                .show().setCanceledOnTouchOutside(false);
    }


    private class ProfilePagerAdapter extends FragmentPagerAdapter {
        public ProfilePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        Fragment[] fragmentArray = new Fragment[]{HilarityUserJokes.newInstance(uid), HilarityUserGenres.newInstance(uid),
                HilarityUserLikes.newInstance(uid)};

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
     * replaces fragment currently shown with another
     * @param fragment is the new fragment to be shown
     */
    public void changeFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.hilarity_content_frame, fragment)
                .addToBackStack(null)
                .commit();
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

    public FloatingActionButton getFAB(){
        return binding.searchFab;
    }
}
