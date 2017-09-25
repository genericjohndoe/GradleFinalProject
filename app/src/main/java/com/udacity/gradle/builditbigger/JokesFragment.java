package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joeljohnson on 7/25/17.
 */

public class JokesFragment extends Fragment {

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mjokesDatabaseReference;
    private ChildEventListener mChildEventListener;

    RecyclerView recyclerview;
    JokesAdapter jokeAdapter;
    List<Joke> jokes;
    String genre;
    String language;


    public JokesFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null){
            genre = extras.getString(getString(R.string.genres));
            language = extras.getString(getString(R.string.languages));
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mjokesDatabaseReference = mFirebaseDatabase.getReference().child(genre);
        jokes = new ArrayList<>();
        jokeAdapter = new JokesAdapter(getActivity(), jokes);
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Joke joke = dataSnapshot.getValue(Joke.class);
                jokes.add(joke);
                jokeAdapter.notifyItemInserted(jokes.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mjokesDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_jokes, container, false);
        recyclerview = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerview.setAdapter(jokeAdapter);
        root.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity()).title(R.string.add_joke)
                        .content(R.string.add_joke_short)
                        .backgroundColorRes(R.color.material_blue_grey_800)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                if (!input.equals("") || !input.equals(null)) {
                                    Joke newJoke = new Joke("new joke", "me", input.toString());
                                    mjokesDatabaseReference.push().setValue(newJoke);
                                }
                            }
                        })
                        .show().setCanceledOnTouchOutside(false);
            }
        });
        return root;
    }
}
