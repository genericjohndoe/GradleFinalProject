package com.udacity.gradle.builditbigger.Collections;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Models.Collection;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;
import com.udacity.gradle.builditbigger.databinding.FragmentGenreBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * DEPRECATED
 */

public class CollectionFragment extends Fragment {
    private String collectionId;
    public CollectionFragment() {}

    public static CollectionFragment newInstance(String collectionId) {
        Bundle args = new Bundle();
        args.putString("collectionId", collectionId);
        CollectionFragment fragment = new CollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) collectionId = getArguments().getString("collectionId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentGenreBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_genre, container, false);
        List<Post> posts = new ArrayList<>();
        JokesAdapter adapter = new JokesAdapter(getActivity(), posts,false);
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        bind.recyclerview.setAdapter(adapter);
        ViewModelProviders.of(this, new CollectedPostsViewModelFactory(collectionId)).get(CollectedPostsViewModel.class)
                .getCollectedPostsLiveData().observe(this, post -> {
                    if (!posts.contains(post)){
                        posts.add(post);
                        adapter.notifyDataSetChanged();
                    }
        });
        return bind.getRoot();
    }


}
