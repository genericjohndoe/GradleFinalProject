package com.udacity.gradle.builditbigger.UserSpecific;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Joke.Joke;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joeljohnson on 11/2/17.
 */

public class HilarityUserMedia extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mjokesDatabaseReference;
    private ChildEventListener mChildEventListener;

    RecyclerView recyclerview;
    EditText searchEditText;
    JokesAdapter jokeAdapter;
    List<Joke> jokes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mjokesDatabaseReference = mFirebaseDatabase.getReference().child(Constants.UID + " Likes");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_jokeslist_genrelist, container, false);
        recyclerview = root.findViewById(R.id.recycler_view);
        //TODO take into account screen size when populating grid
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerview.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerview.setAdapter(jokeAdapter);

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
        return root;
    }
}
