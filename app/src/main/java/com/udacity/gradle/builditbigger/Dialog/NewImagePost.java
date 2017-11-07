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
 * Created by joeljohnson on 11/3/17.
 */

public class NewImagePost extends Fragment {
    //TODO change UI to open camera and show horizontal linear recyclerview below
    Button cameraButton;
    Button galleryButton;
    Button makeMemeButton;
    Button chooseMemeButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_image_post, container, false);
        cameraButton = root.findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO set up intent to camera
            }
        });
        galleryButton = root.findViewById(R.id.gallery_button);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO set up intent to gallery
            }
        });
        chooseMemeButton = root.findViewById(R.id.choose_meme_button);
        chooseMemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO set up intent to in app media selection
            }
        });
        makeMemeButton = root.findViewById(R.id.make_meme_button);
        makeMemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO set up intent to meme creator fragment
            }
        });
        return root;
    }
}
