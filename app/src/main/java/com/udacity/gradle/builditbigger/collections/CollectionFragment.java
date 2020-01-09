package com.udacity.gradle.builditbigger.collections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentGenreBinding;
import com.udacity.gradle.builditbigger.jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.models.Post;

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
