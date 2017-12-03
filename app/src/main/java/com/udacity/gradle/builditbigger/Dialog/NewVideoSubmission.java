package com.udacity.gradle.builditbigger.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.VideoView;

import com.udacity.gradle.builditbigger.R;

/**
 * Created by joeljohnson on 11/21/17.
 */

public class NewVideoSubmission extends Fragment {
    //todo load from firebase
    private VideoView videoPost;
    private EditText tagline;
    private AutoCompleteTextView genre;
    private String filePath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filePath =  getArguments().getString("filepath");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_video_post_submit, container, false);
        videoPost = root.findViewById(R.id.videopost);
        tagline = root.findViewById(R.id.video_tagline);
        genre = root.findViewById(R.id.genre);
        videoPost.setVideoPath(filePath);
        return root;
    }
}
