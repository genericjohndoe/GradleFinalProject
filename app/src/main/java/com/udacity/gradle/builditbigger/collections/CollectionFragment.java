package com.udacity.gradle.builditbigger.collections;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.models.Post;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentGenreBinding;

import java.util.ArrayList;
import java.util.List;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
