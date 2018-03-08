package com.udacity.gradle.builditbigger.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.R;

import java.util.Calendar;

/**
 * Created by joeljohnson on 11/21/17.
 */

public class NewVideoSubmission extends Fragment {
    //todo load from firebase
    private VideoView videoPost;
    private EditText tagline;
    private AutoCompleteTextView genre;
    private String filePath;
    private Button submit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filePath = getArguments().getString("filepath");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_video_post_submit, container, false);
        videoPost = root.findViewById(R.id.videopost);
        tagline = root.findViewById(R.id.video_tagline);
        genre = root.findViewById(R.id.genre);
        videoPost.setVideoPath(filePath);
        submit = root.findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts").push();
                Joke newVideoPost = new Joke("", "", System.currentTimeMillis(),
                        genre.getText().toString(), filePath, Constants.UID, db.getKey(), tagline.getText().toString(),Constants.VIDEO);
                db.setValue(newVideoPost);
                ((NewPostDialog) getActivity().getSupportFragmentManager().findFragmentByTag("dialog")).dismiss();
            }
        });
        return root;
    }
}
