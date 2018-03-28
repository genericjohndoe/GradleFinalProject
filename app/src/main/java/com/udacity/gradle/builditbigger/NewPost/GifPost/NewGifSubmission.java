package com.udacity.gradle.builditbigger.NewPost.GifPost;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.Models.MetaData;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewGifSubmissionBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.droidsonroids.gif.GifDrawable;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewGifSubmission#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewGifSubmission extends Fragment {

    private String filepath;
    private String number;

    public NewGifSubmission() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NewGifSubmission.
     */
    public static NewGifSubmission newInstance(String filepath, String number) {
        NewGifSubmission fragment = new NewGifSubmission();
        Bundle args = new Bundle();
        args.putString("filepath", filepath);
        args.putString("number", number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filepath = getArguments().getString("filepath");
            number = getArguments().getString("number");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentNewGifSubmissionBinding bind = DataBindingUtil.inflate(inflater, R.layout.fragment_new_gif_submission, container, false);
        try {
            bind.gifImageview.setImageDrawable(new GifDrawable(new File(filepath)));
        } catch(IOException e){

        }
        //Glide.with(this).asGif().load(new File(filepath)).into(bind.gifImageview);
        bind.submitButton.setOnClickListener(view -> {
            Constants.STORAGE.child("users/" + Constants.UID + "/gifs/" + getCurrentDateAndTime() + ".gif").putFile(Uri.fromFile(new File(filepath)))
                    .addOnFailureListener(exception -> {
                                Log.i("cloud storage exception", exception.toString());
                    })
                    .addOnSuccessListener(taskSnapshot -> {
                        new File(filepath).delete();
                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                        String tagline = bind.socialEditText.getText().toString();
                        DatabaseReference db = Constants.DATABASE.child("userposts/"+Constants.UID).push();
                        MetaData metaData = new MetaData("gif", Integer.parseInt(number), Constants.getTags(tagline));
                        Joke joke = new Joke("","",System.currentTimeMillis(),"genre", downloadUrl,Constants.UID, db.getKey(), tagline, Constants.GIF,metaData);
                        db.setValue(joke, (databaseError, databaseReference) -> {
                            if (databaseError == null)
                            getActivity().startActivity(new Intent(getActivity(), HilarityActivity.class));
                        });
                    });
        });
        return bind.getRoot();
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

}
