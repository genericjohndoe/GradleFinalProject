package com.udacity.gradle.builditbigger.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.gradle.builditbigger.R;

import java.util.List;

/**
 * Created by joeljohnson on 11/3/17.
 */

public class NewTextPost extends Fragment {

    private EditText title;
    private EditText body;
    private AutoCompleteTextView genreTV;
    private DatabaseReference genrelistRefernce;
    private List<String> genrelist;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        genrelistRefernce = FirebaseDatabase.getInstance().getReference("genrelist");
        genrelistRefernce.addChildEventListener(new ChildEventListener() {
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
        View root = inflater.inflate(R.layout.dialog_new_text_joke, container, false);
        title = root.findViewById(R.id.title_edittext);
        body = root.findViewById(R.id.joke_body_edittext);
        genreTV = root.findViewById(R.id.genre_autoCompleteTextView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, genrelist);
        genreTV.setAdapter(arrayAdapter);
        return root;
    }
}
