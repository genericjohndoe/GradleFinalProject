package com.udacity.gradle.builditbigger.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.udacity.gradle.builditbigger.R;

/**
 * Created by joeljohnson on 11/4/17.
 */

public class NewVideoPost extends Fragment {
    //TODO change UI to open camera and show horizontal linear recyclerview below
    Button shootVidButton;
    Button pickVidButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_video_post, container, false);

        shootVidButton = root.findViewById(R.id.shoot_vid_button);
        shootVidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO set up intent to camera
            }
        });
        pickVidButton = root.findViewById(R.id.pick_vid_button);
        pickVidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO set up intent to gallery
            }
        });
        return root;
    }
}
