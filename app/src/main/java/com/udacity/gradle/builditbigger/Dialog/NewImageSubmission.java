package com.udacity.gradle.builditbigger.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.R;

/**
 * Created by joeljohnson on 11/21/17.
 */

public class NewImageSubmission extends Fragment {
    //todo allow for submission (database and storage)
    //todo try loading from firebase storage instead of external files
    private ImageView imageSubmission;
    private EditText tagline;
    private AutoCompleteTextView genre;
    private String filePath;

    public NewImageSubmission() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filePath = getArguments().getString("filepath");
        Log.i("file name 2", filePath);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_image_post_submit, container, false);
        imageSubmission = root.findViewById(R.id.imagePost);
        tagline = root.findViewById(R.id.image_tagline);
        genre = root.findViewById(R.id.genre);
        //File file = new File(filePath);
        Glide.with(this)
                .load(filePath)
                .into(imageSubmission);
        Toast.makeText(getActivity(), filePath, Toast.LENGTH_SHORT).show();

        return root;
    }
}
