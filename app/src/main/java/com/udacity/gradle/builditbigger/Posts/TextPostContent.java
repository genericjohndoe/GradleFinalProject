package com.udacity.gradle.builditbigger.Posts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.gradle.builditbigger.R;

/**
 * Created by joeljohnson on 11/27/17.
 */

public class TextPostContent extends Fragment {
    TextView title;
    TextView body;
    TextView tags;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.text_post_content_item, container, false);

        title = root.findViewById(R.id.jokeTitle_textView);
        title.setText(getArguments().getString("jokeTitle"));

        body = root.findViewById(R.id.jokeBody_textView);
        body.setText(getArguments().getString("jokeBody"));

        tags = root.findViewById(R.id.tagline_textView);
        tags.setText(getArguments().getString("tagline"));

        return root;
    }
}
