package com.udacity.gradle.builditbigger.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private Button submit;
    private List<String> genrelist;

    public NewImageSubmission() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filePath = getArguments().getString("filepath");
        genrelist = new ArrayList<>();
        Constants.DATABASE.child("genrelist").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String genre = dataSnapshot.getValue(String.class);
                genrelist.add(genre);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_image_post_submit, container, false);
        imageSubmission = root.findViewById(R.id.imagePost);
        tagline = root.findViewById(R.id.image_tagline);
        genre = root.findViewById(R.id.genre);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, genrelist);
        genre.setAdapter(arrayAdapter);
        Glide.with(this)
                .load(filePath)
                .into(imageSubmission);
        submit = root.findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts").push();
                Joke newImagePost = new Joke("", "", System.currentTimeMillis(),
                        genre.getText().toString(), filePath, Constants.UID, db.getKey(), tagline.getText().toString(),Constants.IMAGE);
                db.setValue(newImagePost);
                ((NewPostDialog) getActivity().getSupportFragmentManager().findFragmentByTag("dialog")).dismiss();
            }
        });
        return root;
    }
}
