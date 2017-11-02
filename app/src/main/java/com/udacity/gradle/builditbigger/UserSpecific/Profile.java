package com.udacity.gradle.builditbigger.UserSpecific;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Joke.Joke;
import com.udacity.gradle.builditbigger.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by joeljohnson on 9/28/17.
 */

public class Profile extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private TextView subscribersTextView;
    private TextView subscriptionsTextView;
    private CircleImageView mProfileImageView;
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
        subscribersTextView = root.findViewById(R.id.subscribers_tv);
        subscribersTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //TODO show subscribers fragment
                return false;
            }
        });
        subscriptionsTextView = root.findViewById(R.id.subscriptions_tv);
        subscriptionsTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //TODO show subscriptions fragment
                return false;
            }
        });
        subscribersTextView = root.findViewById(R.id.subscribers_tv);
        mProfileImageView = root.findViewById(R.id.profile_imageview);

        fab =  root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewJokeDialog();
            }
        });

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
       /* mFirebaseAuth = FirebaseAuth.getInstance();
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
        }*/

        return root;
    }

    private void configureFAB(int state){
        if (state == 0){
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   showNewJokeDialog();
                }
            });
        } else if (state == 1){
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showNewGenreDialog();
                }
            });
        } else {
            fab.hide();
        }
    }

    private void showNewJokeDialog(){
        new MaterialDialog.Builder(getActivity()).title(R.string.add_joke)
                .customView(R.layout.activity_new_joke, true)
                .positiveText("Submit")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View view = dialog.getCustomView();
                        String jokeBody = ((EditText) view.findViewById(R.id.title_edittext)).getText().toString();
                        String jokeTitle = ((EditText) view.findViewById(R.id.joke_body_edittext)).getText().toString();
                        Joke joke = new Joke(jokeTitle, Constants.user.getUserName(), jokeBody, ServerValue.TIMESTAMP, "");
                                    /*mjokesDatabaseReference.push().setValue(joke, 0);
                                    mPersonaljokesDatabaseReference.child(Constants.UID + " Jokes").push().setValue(joke,0);*/
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

    private void showNewGenreDialog(){
        new MaterialDialog.Builder(getActivity())
                .title(R.string.add_genre)
                .content(R.string.add_genre_short)
                .backgroundColorRes(R.color.material_blue_grey_800)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                })
                .input(R.string.genre_input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (!input.equals("") || !input.equals(null)) {
                            String newGenre = input.toString();
                                        /*mGenreDatabaseReference.push().setValue(newGenre, 0);
                                        mPersonalGenreDatabaseReference.child(Constants.UID +
                                                " Genres").child(newGenre).setValue(System.currentTimeMillis(),0);*/
                        }
                    }
                })
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .checkBoxPromptRes(R.string.restricted, false, null)
                .show().setCanceledOnTouchOutside(false);
    }

    private class ProfilePagerAdapter extends FragmentPagerAdapter {
        public ProfilePagerAdapter(FragmentManager fm){
            super(fm);
        }

        Fragment[] fragmentArray = new Fragment[]{new HilarityUserJokes(), new HilarityUserGenres(),
                new HilarityUserLikes(), new HilarityUserJokes()};

        String[] tabTitles = new String[]{"Posts", "Genres", "Likes", "Media"};

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
}
