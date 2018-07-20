package com.udacity.gradle.builditbigger.NewPost.TextPost;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewTextPostBinding;
import com.udacity.gradle.builditbigger.Models.Post;

import jp.wasabeef.richeditor.RichEditorToolBar;


/**
 * Created by joeljohnson on 11/3/17.
 */

public class NewTextPostEditFragment extends Fragment {
    public static final String TAGLINE = "tagline";
    public static final String BODY = "body";
    public static final String TITLE = "title";
    public static final String NUMBER = "number";
    FragmentNewTextPostBinding bind;
    private Post post;
    private boolean wasDiscarded = false;
    private boolean saved = false;
    private String number;

    public static NewTextPostEditFragment newInstance(String number) {
        NewTextPostEditFragment newTextPostEditFragment = new NewTextPostEditFragment();
        newTextPostEditFragment.number = number;
        return newTextPostEditFragment;
    }

    public static NewTextPostEditFragment newInstance(Post post) {
        Log.i("iefioejwfw", "post is null 1 : " + (post == null));
        NewTextPostEditFragment newTextPostEditFragment = new NewTextPostEditFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("post", post);
        newTextPostEditFragment.setArguments(bundle);
        return newTextPostEditFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) post = savedInstanceState.getParcelable("post");
        if (getArguments() != null) post = getArguments().getParcelable("post");
        Log.i("iefioejwfw", "post is null 0 : " + (post == null));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_new_text_post, container, false);
        if (post != null) {
            Log.i("iefioejwfw", "post isn't null");
            bind.titleEditText.setText(post.getJokeTitle());
            bind.bodyEditText.setHtml(post.getJokeBody());
            bind.taglineEditText.setText(post.getTagline());
        } else {
            Log.i("iefioejwfw", "post is null");
        }
        bind.continuebutton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), TextPostSubmissionActivity.class);
            if (post != null){
                post.setJokeTitle(bind.titleEditText.getText().toString());
                post.setJokeBody(bind.bodyEditText.getHtml());
                post.setTagline(bind.taglineEditText.getText().toString());
                intent.putExtra("post", post);
            } else {
                intent.putExtra(TITLE, bind.titleEditText.getText().toString());
                intent.putExtra(BODY, bind.bodyEditText.getHtml());
                intent.putExtra(TAGLINE, bind.taglineEditText.getText().toString());
                intent.putExtra(NUMBER, number);
            }
            startActivity(intent);
        });
        RichEditorToolBar toolBar = bind.getRoot().findViewById(R.id.richEditorToolBar);
        toolBar.setRichEditor(bind.bodyEditText);
        bind.bodyEditText.setEditorFontColor("#369F77");
        bind.saveDraftButton.setOnClickListener(view -> { saved = true;
        });
        bind.discardButton.setOnClickListener(view -> {

            startActivity(new Intent(getActivity(), HilarityActivity.class));
        });

        return bind.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (saved) {
            post.setJokeTitle(bind.titleEditText.getText().toString());
            post.setJokeBody(bind.bodyEditText.getHtml());
            post.setTagline(bind.taglineEditText.getText().toString());
            outState.putParcelable("post", post);
        }
        super.onSaveInstanceState(outState);
    }

}
