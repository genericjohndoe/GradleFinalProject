package com.udacity.gradle.builditbigger.UserSpecific;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Genres.Genre;
import com.udacity.gradle.builditbigger.Genres.GenreAdapter;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joeljohnson on 10/12/17.
 */

public class HilarityUserGenres extends Fragment {

    RecyclerView recyclerview;
    //EditText searchEditText;
    ImageView noItems;
    GenreAdapter genreAdapter;
    List<Genre> genres;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        genres = new ArrayList<>();
        Constants.DATABASE.child("usergenres/" + Constants.UID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Genre genre = dataSnapshot.getValue(Genre.class);
                genres.add(genre);
                genreAdapter.notifyDataSetChanged();
                configureUI();
                if (recyclerview != null) recyclerview.scrollToPosition(genres.size()-1);
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
        genreAdapter = new GenreAdapter(getActivity(), genres);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_jokeslist_genrelist, container, false);
        noItems = root.findViewById(R.id.no_item_imageview);

        recyclerview = root.findViewById(R.id.recycler_view);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        recyclerview.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerview.setAdapter(genreAdapter);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0) {
                    //TODO hide profile fragment fab
                    //((Profile) getParentFragment()).hideFab();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //TODO SHOW profile fragment fab
                    //((Profile) getParentFragment()).showFab();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


//        searchEditText = root.findViewById(R.id.search_et);
//        searchEditText.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                // If the event is a key-down event on the "enter" button
//                if ((event.getAction() == KeyEvent.ACTION_DOWN)
//                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    mgr.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
//                    return true;
//                }
//                return false;
//            }
//        });
        configureUI();
        return root;
    }

    public void configureUI() {
        if (genres.isEmpty()) {
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
