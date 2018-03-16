package com.udacity.gradle.builditbigger.Feed;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
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
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;
import com.udacity.gradle.builditbigger.Interfaces.VideoCallback;
import com.udacity.gradle.builditbigger.databinding.FeedExplorePageBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * CLASS SHOWS post of followed users
 */

public class FeedFragment extends Fragment {

    JokesAdapter jokeAdapter;
    List<Joke> jokes;
    FeedExplorePageBinding bind;
    private String uid;

    public static FeedFragment newInstance(String uid){
        FeedFragment feedFragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        feedFragment.setArguments(bundle);
        return feedFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getArguments().getString("uid");
        jokes = new ArrayList<>();
        jokeAdapter = new JokesAdapter(getActivity(), jokes,false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater,R.layout.feed_explore_page, container, false);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        bind.recyclerView.setLayoutManager(llm);
        bind.recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        bind.recyclerView.setAdapter(jokeAdapter);
        FeedViewModel feedViewModel = ViewModelProviders.of(this, new FeedViewModelProvider(uid)).get(FeedViewModel.class);
        feedViewModel.getFeedLiveData().observe(this, joke -> {
            if (!jokes.contains(joke)) {
                jokes.add(joke);
                jokeAdapter.notifyDataSetChanged();
                bind.recyclerView.scrollToPosition(jokes.size() - 1);
            }
            //configureUI();
        });
        configureUI();
        return bind.getRoot();
    }

    public void configureUI() {
        if (jokes.isEmpty()) {
            bind.recyclerView.setVisibility(View.GONE);
            bind.noItemImageview.setVisibility(View.VISIBLE);
        } else {
            bind.recyclerView.setVisibility(View.VISIBLE);
            bind.noItemImageview.setVisibility(View.GONE);
        }
    }
}
