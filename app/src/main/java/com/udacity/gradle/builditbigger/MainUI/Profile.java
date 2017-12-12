package com.udacity.gradle.builditbigger.MainUI;

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
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Dialog.NewPostDialog;
import com.udacity.gradle.builditbigger.Genres.Genre;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.UserSpecific.HilarityUserGenres;
import com.udacity.gradle.builditbigger.UserSpecific.HilarityUserJokes;
import com.udacity.gradle.builditbigger.UserSpecific.HilarityUserLikes;
import com.udacity.gradle.builditbigger.UserSpecific.SubscribersFragment;
import com.udacity.gradle.builditbigger.UserSpecific.SubscriptionsFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by joeljohnson on 9/28/17.
 */

public class Profile extends Fragment {
    //todo populate UI with info from database
    //todo switch subs buttons with button and null background
    //todo find out why viewpager fragments don't immediately show when profile page is reloaded
    //todo convert svg to text use in viewpager

    private TabLayout tabLayout;
    private ViewPager viewPager;
    //private FloatingActionButton fab;
    private FloatingActionMenu fam;
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

        viewPager = root.findViewById(R.id.profile_view_pager);
        viewPager.setAdapter(new ProfilePagerAdapter(getActivity().getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                /*configureFAB(position);*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        tabLayout = root.findViewById(R.id.profile_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        subscribersTextView = root.findViewById(R.id.subscribers_tv);
        subscribersTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //todo find out why 2 taps are needed to go back, maybe replace FT with intent for activity
                view.performClick();
                changeFragment(new SubscribersFragment());
                return false;
            }
        });

        subscriptionsTextView = root.findViewById(R.id.subscriptions_tv);
        subscriptionsTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.performClick();
                changeFragment(new SubscriptionsFragment());
                return false;
            }
        });

        Constants.DATABASE.child("userposts/" + Constants.UID + "/NumPosts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long number = dataSnapshot.getValue(Long.class);
                numberOfPosts.setText(number + " posts");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Constants.DATABASE.child("following/" + Constants.UID + "/num").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long number = dataSnapshot.getValue(Long.class);
                subscriptionsTextView.setText(number + " subscriptions");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Constants.DATABASE.child("followers/" + Constants.UID + "/num").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long number = dataSnapshot.getValue(Long.class);
                subscribersTextView.setText(number + " subscribers");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



//        fab = root.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showNewJokeDialog();
//            }
//        });

        mProfileImageView = root.findViewById(R.id.profile_imageview);
        getActivity().setTitle(Constants.USER.getUserName());
        Glide.with(this)
                .load(Constants.USER.getUrlString())
                .into(mProfileImageView);

        return root;
    }

//    private void configureFAB(int state) {
//        if (state == 0) {
//            fab.show();
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    showNewJokeDialog();
//                }
//            });
//        } else if (state == 1) {
//            fab.show();
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    showNewGenreDialog();
//                }
//            });
//        } else {
//            fab.hide();
//        }
//    }

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
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("usergenres/" + Constants.UID).push();
                        Genre newGenre = new Genre(genreTitle, Constants.USER.getUserName(), isRestricted, genreLanguage.toString(),
                                new Date(System.currentTimeMillis()), Constants.USER.getUID(), db.getKey());
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

    /*public void hideFab() {
        fab.hide();
    }

    public void showFab() {
        fab.show();
    }*/

}
