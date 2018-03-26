package com.udacity.gradle.builditbigger.Jokes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * DEPRECATED
 */

public class JokesFragment extends Fragment {

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mjokesDatabaseReference;
    private DatabaseReference mPersonaljokesDatabaseReference;
    private ChildEventListener mChildEventListener;

    RecyclerView recyclerview;
    JokesAdapter jokeAdapter;
    List<Joke> jokes;
    String genre;
    String language;


    public JokesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            genre = extras.getString(getString(R.string.genres));
            language = extras.getString(getString(R.string.languages));
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mjokesDatabaseReference = mFirebaseDatabase.getReference().child(genre);
        mPersonaljokesDatabaseReference = mFirebaseDatabase.getReference();
        jokes = new ArrayList<>();
        //jokeAdapter = new JokesAdapter(getActivity(), jokes);
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Joke joke = dataSnapshot.getValue(Joke.class);
                jokes.add(0, joke);
                jokeAdapter.notifyDataSetChanged();
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
        };
        mjokesDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_jokeslist_genrelist, container, false);
        recyclerview = root.findViewById(R.id.recycler_view);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerview.setAdapter(jokeAdapter);;
        return root;
    }
}
