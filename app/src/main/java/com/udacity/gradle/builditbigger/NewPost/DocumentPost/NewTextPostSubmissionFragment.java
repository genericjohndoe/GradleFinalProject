package com.udacity.gradle.builditbigger.NewPost.DocumentPost;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.MetaData;
import com.udacity.gradle.builditbigger.Models.Post;
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
    private Post post;

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

    public static NewTextPostSubmissionFragment newInstance(Post post) {
        NewTextPostSubmissionFragment fragment = new NewTextPostSubmissionFragment();
        Bundle args = new Bundle();
        args.putParcelable("post", post);
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
            post = getArguments().getParcelable("post");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNewTextPostSubmissionBinding bind = DataBindingUtil.inflate(inflater,
                R.layout.fragment_new_text_post_submission, container, false);
        if (post != null) {
            bind.titleTextView.setText(post.getJokeTitle());
            bind.bodyTextView.setText(post.getJokeBody());
            bind.socialTextView.setText(post.getTagline());
        } else {
            bind.titleTextView.setText(title);
            bind.bodyTextView.setText(body);
            bind.socialTextView.setText(tagline);
        }
        bind.bodyTextView.setMovementMethod(new ScrollingMovementMethod());

        bind.submitButton.setOnClickListener(view -> {
            DatabaseReference db;
            if (post != null) {
                db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts/" + post.getPushId());
                db.setValue(post);
                startActivity(new Intent(getActivity(), HilarityActivity.class));
                getActivity().finish();
            } else {
                Post newJoke = new Post(title, body, System.currentTimeMillis(),
                        "genre push id", "", Constants.UID, null, tagline, Constants.TEXT,
                        new MetaData("text", Integer.parseInt(number) + 1, Constants.getTags(tagline)));
                db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts").push();
                newJoke.setPushId(db.getKey());
                db.setValue(newJoke, (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        startActivity(new Intent(getActivity(), HilarityActivity.class));
                        Constants.DATABASE.child("userposts/" + Constants.UID + "/num").setValue(Integer.parseInt(number) + 1);
                        Constants.DATABASE.child("userpostslikescomments/" + Constants.UID + "/" + databaseReference.getKey() + "/comments/num").setValue(0);
                        Constants.DATABASE.child("userpostslikescomments/" + Constants.UID + "/" + databaseReference.getKey() + "/likes/num").setValue(0);
                    }
                });
            }
        });
        bind.editButton.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        bind.discardButton.setOnClickListener(view -> {
            getActivity().startActivity(new Intent(getActivity(), HilarityActivity.class));
        });
        return bind.getRoot();
    }


}
