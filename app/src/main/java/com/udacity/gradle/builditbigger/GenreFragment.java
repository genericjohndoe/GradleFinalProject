package com.udacity.gradle.builditbigger;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by joeljohnson on 7/25/17.
 */

public class GenreFragment extends Fragment  {

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGenreDatabaseReference;
    private ChildEventListener mChildEventListener;

    RecyclerView recyclerview;
    GenreAdapter genreAdapter;
    List<String> genres;
    String langaugeGenre = "";

    public GenreFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("GF", "GF started");

//        Intent intent = getActivity().getIntent();
//        Bundle extras = intent.getExtras();
//        if (extras != null && !extras.getString(getString(R.string.languages)).equals(null)){
//            langaugeGenre = extras.getString(getString(R.string.languages));
//            Log.i("jokes", "pulled from intent");
//        } else {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            Set<String> set = sharedPref.getStringSet(getString(R.string.preference_saved_languages_set), null);
            langaugeGenre = set.toArray()[0] + " Genres";
            Log.i("jokes", "pulled from set");
            //langaugeGenre = "English Genres";
        //}

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mGenreDatabaseReference = mFirebaseDatabase.getReference().child(langaugeGenre);
        if (mGenreDatabaseReference == null) Log.i("GF", "Database reference is null");
        genres = new ArrayList<>();
        genreAdapter = new GenreAdapter(getActivity(),genres);
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String genre = dataSnapshot.getValue(String.class);
                genres.add(0,genre);
                genreAdapter.notifyDataSetChanged();
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
        mGenreDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_genre, container, false);
        recyclerview = root.findViewById(R.id.genre_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerview.setAdapter(genreAdapter);
        root.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.add_genre)
                        .content(R.string.add_genre_short)
                        .backgroundColorRes(R.color.material_blue_grey_800)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .dismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                            }
                        })
                        .input(R.string.genre_input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                if (!input.equals("") || !input.equals(null)) {
                                    String newGenre = input.toString();
                                    mGenreDatabaseReference.push().setValue(newGenre, 0);
                                }
                            }
                        })
                        .negativeText(R.string.cancel)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                         })
                        .checkBoxPromptRes(R.string.restricted, false, null)
                        .show().setCanceledOnTouchOutside(false);
            }
        });
        return root;
    }


}
