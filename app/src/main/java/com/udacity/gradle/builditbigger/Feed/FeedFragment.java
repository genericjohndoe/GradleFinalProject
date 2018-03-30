package com.udacity.gradle.builditbigger.Feed;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;
import com.udacity.gradle.builditbigger.databinding.FeedExplorePageBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * CLASS SHOWS posts of followed users
 */

public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    JokesAdapter jokeAdapter;
    List<Joke> jokes;
    FeedExplorePageBinding bind;
    private String uid;
    private boolean enableSwipeToRefresh = false;

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
        bind.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0){
                    bind.fab.hide(true);
                    enableSwipeToRefresh = true;
                    bind.refreshButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) bind.fab.show(true);
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        bind.fab.setOnClickListener(view ->{});//todo search on feed
        FeedViewModel feedViewModel = ViewModelProviders.of(this, new FeedViewModelProvider(uid)).get(FeedViewModel.class);
        feedViewModel.getFeedLiveData().observe(this, joke -> {
            if (!jokes.contains(joke)) {
                jokes.add(joke);
                if (!enableSwipeToRefresh){
                    refreshLayout();
                } else {
                    bind.refreshButton.setVisibility(View.VISIBLE);
                }
            }
            if (jokes.size() == 0 || jokes.size() == 1) configureUI();
        });
        bind.refreshButton.setOnClickListener(view -> refreshLayout());
        bind.swipe.setOnRefreshListener(this);
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

    @Override
    public void onRefresh() {
        refreshLayout();
    }

    public void refreshLayout(){
        jokeAdapter.notifyDataSetChanged();
        bind.recyclerView.scrollToPosition(jokes.size() - 1);
    }
}
