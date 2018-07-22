package com.udacity.gradle.builditbigger.Explore;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Search.SearchActivity;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;
import com.udacity.gradle.builditbigger.databinding.FeedExplorePageBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * shows random posts
 */

public class ExploreFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FeedExplorePageBinding bind;
    JokesAdapter jokeAdapter;
    List<Post> jokes;
    private String uid;
    private boolean enableSwipeToRefresh = false;

    public ExploreFragment() {}

    public static ExploreFragment newInstance(String uid){
        ExploreFragment exploreFragment = new ExploreFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        exploreFragment.setArguments(bundle);
        return exploreFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jokes = new ArrayList<>();
        uid = getArguments().getString("uid");
        jokeAdapter = new JokesAdapter(getActivity(), jokes, false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater,R.layout.feed_explore_page, container, false);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        //llm.setStackFromEnd(true);
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

        bind.fab.setOnClickListener(view -> {getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));});
        ExploreViewModel exploreViewModel = ViewModelProviders.of(this).get(ExploreViewModel.class);
        exploreViewModel.getExploreLiveData().observe(this, joke -> {
            if (!jokes.contains(joke)) {
                jokes.add(joke);
                if (!enableSwipeToRefresh){
                    refreshLayout();
                } else {
                    bind.refreshButton.setVisibility(View.VISIBLE);
                }
            }
            if (jokes.size() == 1 || jokes.size() == 0) configureUI();
        });

        bind.refreshButton.setOnClickListener(view -> refreshLayout());
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
