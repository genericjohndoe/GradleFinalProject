package com.udacity.gradle.builditbigger.UserSpecific;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.HideFAB;
import com.udacity.gradle.builditbigger.Joke.Joke;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joeljohnson on 10/12/17.
 */

public class HilarityUserJokes extends Fragment {

    RecyclerView recyclerview;
    //EditText searchEditText;
    ImageView noItems;

    JokesAdapter jokeAdapter;
    List<Joke> jokes;
    HideFAB conFam;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jokes = new ArrayList<>();
        Constants.DATABASE.child("userposts/" + Constants.UID + "/posts")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Joke joke = dataSnapshot.getValue(Joke.class);
                        jokes.add(joke);
                        Log.i("joke size", jokes.size() + "");
                        jokeAdapter.notifyDataSetChanged();
                        configureUI();
                        if (recyclerview != null) recyclerview.scrollToPosition(jokes.size()-1);
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
        jokeAdapter = new JokesAdapter(getActivity(), jokes);
        conFam = (HideFAB) getActivity().getSupportFragmentManager().findFragmentByTag("profile");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_jokeslist_genrelist, container, false);
        noItems = root.findViewById(R.id.no_item_imageview);

        recyclerview = root.findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        llm.setStackFromEnd(true);
        recyclerview.setLayoutManager(llm);

        recyclerview.setAdapter(jokeAdapter);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0) {
                    conFam.hideFAB();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    conFam.showFAB();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        configureUI();
        return root;
    }

    public void configureUI() {
        if (jokes.isEmpty()) {
            recyclerview.setVisibility(View.GONE);
            noItems.setVisibility(View.VISIBLE);
            //searchEditText.setVisibility(View.GONE);
        } else {
            recyclerview.setVisibility(View.VISIBLE);
            noItems.setVisibility(View.GONE);
            //searchEditText.setVisibility(View.VISIBLE);
        }
    }
}
