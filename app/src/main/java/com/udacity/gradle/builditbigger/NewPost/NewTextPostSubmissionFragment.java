package com.udacity.gradle.builditbigger.NewPost;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.Models.MetaData;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewTextPostSubmissionBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewTextPostSubmissionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTextPostSubmissionFragment extends Fragment {
    private static final String TITLE = "title";
    private static final String BODY = "body";
    public static final String TAGLINE = "tagline";
    public static final String NUMBER = "number";

    private String title;
    private String body;
    private String tagline;
    private String number;

    public NewTextPostSubmissionFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewTextPostSubmissionFragment.
     */
    public static NewTextPostSubmissionFragment newInstance(String title, String body, String tagline, String number) {
        NewTextPostSubmissionFragment fragment = new NewTextPostSubmissionFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(BODY, body);
        args.putString(TAGLINE, tagline);
        args.putString(NUMBER, number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            body = getArguments().getString(BODY);
            tagline = getArguments().getString(TAGLINE);
            number = getArguments().getString(NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNewTextPostSubmissionBinding bind = DataBindingUtil.inflate(inflater,
                R.layout.fragment_new_text_post_submission, container, false);
        bind.titleTextView.setText(title);
        bind.bodyTextView.setText(body);
        bind.bodyTextView.setMovementMethod(new ScrollingMovementMethod());
        bind.socialTextView.setText(tagline);
        bind.submitButton.setOnClickListener(view -> {
            DatabaseReference db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts").push();
            Joke newJoke = new Joke(title, body, System.currentTimeMillis(),
                    "genre push id", "", Constants.UID, db.getKey(), tagline, Constants.TEXT,
                    new MetaData("text", Integer.parseInt(number) + 1,Constants.getTags(tagline)));
            db.setValue(newJoke, (databaseError, databaseReference) -> {
                if (databaseError != null)
                    Log.i("Hilarity", "database error " + databaseError.getMessage());
                Constants.changeFragment(R.id.hilarity_content_frame,
                        Profile.newInstance(Constants.UID), (AppCompatActivity) getActivity());
            });
        });
        bind.editButton.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        bind.discardButton.setOnClickListener(view -> {
            Constants.changeFragment(R.id.hilarity_content_frame,
                    Profile.newInstance(Constants.UID), (AppCompatActivity) getActivity());
        });
        return bind.getRoot();
    }


}
