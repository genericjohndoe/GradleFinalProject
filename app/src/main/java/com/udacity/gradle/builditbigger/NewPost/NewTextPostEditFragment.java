package com.udacity.gradle.builditbigger.NewPost;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewTextPostBinding;

/**
 * Created by joeljohnson on 11/3/17.
 */

public class NewTextPostEditFragment extends Fragment {
    public static final String TAGLINE = "tagline";
    public static final String BODY = "body";
    public static final String TITLE = "title";
    FragmentNewTextPostBinding bind;
    private String title;
    private String body;
    private String tagline;
    private boolean wasDiscarded = false;
    private String number;

    public static NewTextPostEditFragment newInstance(String number) {
        NewTextPostEditFragment newTextPostEditFragment = new NewTextPostEditFragment();
        newTextPostEditFragment.number = number;
        return newTextPostEditFragment;
    }

    public static NewTextPostEditFragment newInstance(String title, String body, String tagline) {
        NewTextPostEditFragment newTextPostEditFragment = new NewTextPostEditFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putString(BODY, body);
        bundle.putString(TAGLINE, tagline);
        newTextPostEditFragment.setArguments(bundle);
        return new NewTextPostEditFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            title = savedInstanceState.getString(TITLE);
            body = savedInstanceState.getString(BODY);
            tagline = savedInstanceState.getString(TAGLINE);
        }
        if (getArguments() != null){
            title = getArguments().getString(TITLE);
            body = getArguments().getString(BODY);
            tagline = getArguments().getString(TAGLINE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_new_text_post, container, false);
        if (title != null) {
            bind.titleEditText.setText(title);
            bind.bodyEditText.setText(body);
            bind.taglineEditText.setText(tagline);
        }
        bind.continuebutton.setOnClickListener(view -> {
            Constants.changeFragment(R.id.hilarity_content_frame, NewTextPostSubmissionFragment.newInstance(bind.titleEditText.getText().toString(),
                    bind.bodyEditText.getText().toString(), bind.taglineEditText.getText().toString(), number), (AppCompatActivity) getActivity());
        });
        bind.saveDraftButton.setOnClickListener(view -> {//todo add path for saved drafts in database
        });
        bind.discardButton.setOnClickListener(view -> {
            wasDiscarded = true;
            Constants.changeFragment(R.id.hilarity_content_frame,
                    Profile.newInstance(Constants.UID), (AppCompatActivity) getActivity());
        });
        return bind.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!wasDiscarded) {
            outState.putString(TITLE, bind.titleEditText.getText().toString());
            outState.putString(BODY, bind.bodyEditText.getText().toString());
            outState.putString(TAGLINE, bind.taglineEditText.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }
}
