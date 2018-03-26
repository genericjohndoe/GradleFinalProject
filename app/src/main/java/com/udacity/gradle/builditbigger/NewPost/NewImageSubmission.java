package com.udacity.gradle.builditbigger.NewPost;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.Models.MetaData;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewImagePostSubmissionBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by joeljohnson on 11/21/17.
 */

public class NewImageSubmission extends Fragment {
    private File file;
    private String number;

    public NewImageSubmission() {
    }

    public static NewImageSubmission newInstance(File file, String number) {
        NewImageSubmission fragment = new NewImageSubmission();
        fragment.file = file;
        fragment.number = number;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentNewImagePostSubmissionBinding bind = DataBindingUtil.inflate(inflater, R.layout.fragment_new_image_post_submission, container, false);
        Glide.with(this)
                .load(file)
                .into(bind.imagePost);
        bind.submitButton.setOnClickListener(view -> {
                    Constants.STORAGE.child("users/" + Constants.UID + "/images/" + getCurrentDateAndTime() + ".png").putFile(Uri.fromFile(file))
                            .addOnFailureListener(exception -> {
                                Log.i("cloud storage exception", exception.toString());
                                    }
                            )
                            .addOnSuccessListener((taskSnapshot) -> {
                                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                                        DatabaseReference db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts").push();
                                        Joke newImagePost = new Joke("", "", System.currentTimeMillis(),
                                                "genre push id", downloadUrl, Constants.UID,
                                                db.getKey(), bind.imageTagline.getText().toString(), Constants.IMAGE,
                                                new MetaData("image", Integer.parseInt(number)+1,Constants.getTags(bind.imageTagline.getText().toString())));
                                        db.setValue(newImagePost);
                                    }
                            );
                }
        );
        return bind.getRoot();
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
