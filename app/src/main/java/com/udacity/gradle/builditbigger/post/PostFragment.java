package com.udacity.gradle.builditbigger.post;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeBinding;

/**
 * DEPRECATED
 */

public class PostFragment extends Fragment {
    String body;
    String title;

    public static PostFragment newInstance(String title, String body, Context context) {
        Bundle args = new Bundle();
        PostFragment fragment = new PostFragment();
        args.putString(context.getString(R.string.title),title);
        args.putString(context.getString(R.string.body),body);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(getString(R.string.title));
            body = getArguments().getString(getString(R.string.body));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentJokeBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_joke, container, false);
        binding.jokeTitle.setText(title);
        binding.jokeBody.setText(Html.fromHtml(body));
        return binding.getRoot();
    }
}
