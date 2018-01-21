package com.udacity.gradle.builditbigger.MainUI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Dialog.NewPostDialog;
import com.udacity.gradle.builditbigger.Models.Genre;
import com.udacity.gradle.builditbigger.HideFAB;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.UserInfoViewModel;
import com.udacity.gradle.builditbigger.UserInfoViewModelFactory;
import com.udacity.gradle.builditbigger.UserSpecific.HilarityUserGenres;
import com.udacity.gradle.builditbigger.UserSpecific.HilarityUserJokes;
import com.udacity.gradle.builditbigger.UserSpecific.HilarityUserLikes;
import com.udacity.gradle.builditbigger.UserSpecific.SubscribersFragment;
import com.udacity.gradle.builditbigger.UserSpecific.SubscriptionsFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by joeljohnson on 9/28/17.
 */

public class Profile extends Fragment implements HideFAB {
    //todo populate UI with info from database
    //todo switch subs buttons with button and null background
    //todo find out why viewpager fragments don't immediately show when profile page is reloaded
    //todo convert svg to text use in viewpager

    private TabLayout tabLayout;
    private ViewPager viewPager;
    //private FloatingActionButton fab;
    private FloatingActionMenu fam;
    private FloatingActionButton searchFab;
    private FloatingActionButton newPostFab;
    private TextView subscribersTextView;
    private TextView subscriptionsTextView;
    private TextView numberOfPosts;
    private CircleImageView mProfileImageView;
    private List<String> languages = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        numberOfPosts = root.findViewById(R.id.posts_tv);
        mProfileImageView = root.findViewById(R.id.profile_imageview);
        newPostFab = root.findViewById(R.id.new_post_fab);
        viewPager = root.findViewById(R.id.profile_view_pager);
        tabLayout = root.findViewById(R.id.profile_tab_layout);
        subscribersTextView = root.findViewById(R.id.subscribers_tv);
        subscriptionsTextView = root.findViewById(R.id.subscriptions_tv);
        fam = root.findViewById(R.id.fam);
        searchFab = root.findViewById(R.id.search_fab);

        viewPager.setAdapter(new ProfilePagerAdapter(getActivity().getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                configureFAB(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        tabLayout.setupWithViewPager(viewPager);

        subscribersTextView.setOnClickListener(view ->{
            changeFragment(new SubscribersFragment());
        });

        subscriptionsTextView.setOnClickListener(view -> {
            changeFragment(new SubscriptionsFragment());
        });

        searchFab.setOnClickListener(view ->  {
                new MaterialDialog.Builder(getActivity())
                        .customView(R.layout.search, true)
                        .positiveText("Search")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                View view = dialog.getCustomView();
                                String searchKeyword = ((EditText) view.findViewById(R.id.search)).getText().toString();
                                //todo identify which fragment is showing
                                //todo on server side when post is created, add list of tags, check to see if tag exist
                                //todo parse through part of database looking for keyword
                                //todo modify recyclerview to show only returned results
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show().setCanceledOnTouchOutside(false);
            }
        );

        newPostFab.setOnClickListener(view -> showNewJokeDialog());

        UserInfoViewModel userInfoViewModel = ViewModelProviders.of(this,
                new UserInfoViewModelFactory(Constants.UID))
                .get(UserInfoViewModel.class);

        userInfoViewModel.getUserName().observe(this, (dataSnapshot) -> {
                String userName = dataSnapshot.getValue(String.class);
                getActivity().setTitle(userName);
            }
        );

        userInfoViewModel.getUserProfileImg().observe(this, (dataSnapshot ->{
            String profileUrl = dataSnapshot.getValue(String.class);
            Glide.with(Profile.this)
                    .load(profileUrl)
                    .into(mProfileImageView);
            })
        );

        userInfoViewModel.getNumPostLiveData().observe(this, (dataSnapshot -> {
            Long number = dataSnapshot.getValue(Long.class);
            numberOfPosts.setText(number + " posts");
            })
        );

        userInfoViewModel.getNumFollowingLiveData().observe(this, dataSnapshot -> {
            Long number = dataSnapshot.getValue(Long.class);
            subscriptionsTextView.setText(number + " subscriptions");
        });

        userInfoViewModel.getNumFollowersLiveData().observe(this, dataSnapshot -> {
            Long number = dataSnapshot.getValue(Long.class);
            subscribersTextView.setText(number + " subscribers");
        });
        return root;
    }

    private void configureFAB(int state) {
        if (state == 0) {
            fam.showMenu(true);
            newPostFab.setOnClickListener(view -> showNewJokeDialog());
        } else if (state == 1) {
            fam.showMenu(true);
            newPostFab.setOnClickListener(view -> showNewGenreDialog());
        } else {
            fam.hideMenu(true);
        }
    }

    private void showNewJokeDialog() {
        new NewPostDialog().show(getActivity().getSupportFragmentManager(), "dialog");
    }

    private void showNewGenreDialog() {
        new MaterialDialog.Builder(getActivity())
                .customView(R.layout.dialog_new_genre, true)
                .positiveText("Submit")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View view = dialog.getCustomView();
                        String genreTitle = ((EditText) view.findViewById(R.id.new_genre_title_et)).getText().toString();
                        boolean isRestricted = ((CheckBox) view.findViewById(R.id.restricted_checkBox)).isChecked();
                        getLanguageList();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
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

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show().setCanceledOnTouchOutside(false);
    }

    private class ProfilePagerAdapter extends FragmentPagerAdapter {
        public ProfilePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        Fragment[] fragmentArray = new Fragment[]{new HilarityUserJokes(), new HilarityUserGenres(),
                new HilarityUserLikes()};

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

    private void getLanguageList() {
        FirebaseDatabase.getInstance().getReference("Languages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String lang = dataSnapshot.getValue(String.class);
                languages.add(lang);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void changeFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.hilarity_content_frame, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void hideFAB() {
        fam.hideMenu(true);
    }

    public void showFAB() {
        fam.showMenu(true);
    }


}
