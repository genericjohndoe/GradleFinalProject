package com.udacity.gradle.builditbigger.Posts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.R;

/**
 * Created by joeljohnson on 12/2/17.
 */

public class ImagePostContent extends Fragment {

    ImageView image;
    TextView tags;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.text_post_content_item, container, false);

        image = root.findViewById(R.id.post_imageview);
        Glide.with(this).load(getArguments().getString("media_url")).into(image);

        tags = root.findViewById(R.id.tagline_textView);
        tags.setText(getArguments().getString("tagline"));

        return root;
    }
}
