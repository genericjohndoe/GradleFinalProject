package com.udacity.gradle.builditbigger.explore;

import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;
import com.udacity.gradle.builditbigger.databinding.FeedExplorePageBinding;
import com.udacity.gradle.builditbigger.jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.models.Post;
import com.udacity.gradle.builditbigger.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * shows random posts
 */

public class ExploreFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FeedExplorePageBinding bind;
    private JokesAdapter jokeAdapter;
    private List<Post> jokes;
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

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, true);
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
                    //bind.refreshButton.setVisibility(View.GONE);
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
                    //bind.refreshButton.setVisibility(View.VISIBLE);
                }
            }
            if (jokes.size() == 1 || jokes.size() == 0) configureUI();
        });

        //bind.refreshButton.setOnClickListener(view -> refreshLayout());
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
