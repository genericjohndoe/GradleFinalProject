package com.udacity.gradle.builditbigger.MainUI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Joke.Joke;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;
import com.udacity.gradle.builditbigger.VideoCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joeljohnson on 10/30/17.
 */

public class ExploreFragment extends Fragment implements VideoCallback {

    RecyclerView recyclerview;
    EditText searchEditText;
    ImageView noItems;
    JokesAdapter jokeAdapter;
    List<Joke> jokes;

    public ExploreFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jokes = new ArrayList<>();
        Constants.DATABASE.child("allposts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Joke joke = dataSnapshot.getValue(Joke.class);
                jokes.add(joke);
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
        });
        jokeAdapter = new JokesAdapter(getActivity(), jokes, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.feed_explore_page, container, false);
        noItems = root.findViewById(R.id.no_item_imageview);
        recyclerview = root.findViewById(R.id.recycler_view);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        recyclerview.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerview.setAdapter(jokeAdapter);
        configureUI();
        searchEditText = root.findViewById(R.id.search_et);
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        return root;
    }

    public void configureUI() {
        if (jokes.isEmpty()) {
            recyclerview.setVisibility(View.GONE);
            noItems.setVisibility(View.VISIBLE);
        } else {
            recyclerview.setVisibility(View.VISIBLE);
            noItems.setVisibility(View.GONE);
        }
    }

    @Override
    public void getVideoInfo(boolean started, int position) {

    }

    @Override
    public void onVideoPostRecycled(long id) {

    }

    @Override
    public void setCurrentlyPlaying(long id) {

    }

    @Override
    public void onNewVideoPost(long id) {

    }
}
