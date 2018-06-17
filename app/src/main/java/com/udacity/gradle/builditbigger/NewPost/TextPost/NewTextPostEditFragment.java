package com.udacity.gradle.builditbigger.NewPost.TextPost;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewTextPostBinding;

import jp.wasabeef.richeditor.RichEditorToolBar;


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
            bind.bodyEditText.setHtml(body);
            bind.taglineEditText.setText(tagline);
        }
        bind.continuebutton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), TextPostSubmissionActivity.class);
            intent.putExtra("title", bind.titleEditText.getText().toString());
            intent.putExtra("body", bind.bodyEditText.getHtml());
            intent.putExtra("tagline", bind.taglineEditText.getText().toString());
            intent.putExtra("number", number);
            startActivity(intent);
        });
        RichEditorToolBar toolBar = bind.getRoot().findViewById(R.id.richEditorToolBar);
        toolBar.setRichEditor(bind.bodyEditText);
        bind.bodyEditText.setEditorFontColor("#369F77");
        bind.saveDraftButton.setOnClickListener(view -> {//todo add path for saved drafts in database
        });
        bind.discardButton.setOnClickListener(view -> {
            wasDiscarded = true;
            startActivity(new Intent(getActivity(), HilarityActivity.class));
        });

        return bind.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!wasDiscarded) {
            outState.putString(TITLE, bind.titleEditText.getText().toString());
            outState.putString(BODY, bind.bodyEditText.getHtml());
            outState.putString(TAGLINE, bind.taglineEditText.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

}
