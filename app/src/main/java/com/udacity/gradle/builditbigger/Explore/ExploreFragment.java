package com.udacity.gradle.builditbigger.Explore;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Feed.FeedViewModel;
import com.udacity.gradle.builditbigger.Feed.FeedViewModelProvider;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Search.SearchFragment;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;
import com.udacity.gradle.builditbigger.Interfaces.VideoCallback;
import com.udacity.gradle.builditbigger.databinding.FeedExplorePageBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * shows random posts
 */

public class ExploreFragment extends Fragment {

    FeedExplorePageBinding bind;
    JokesAdapter jokeAdapter;
    List<Joke> jokes;
    private String uid;

    public ExploreFragment() {
    }

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
                if (dy > 0 || dy < 0) bind.fab.hide(true);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) bind.fab.show(true);
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        bind.fab.setOnClickListener(view ->{Constants.changeFragment(R.id.hilarity_content_frame,
                SearchFragment.newInstance(), (AppCompatActivity) getActivity());});
        ExploreViewModel exploreViewModel = ViewModelProviders.of(this).get(ExploreViewModel.class);
        exploreViewModel.getExploreLiveData().observe(this, joke -> {
            if (!jokes.contains(joke)) {
                jokes.add(joke);
                jokeAdapter.notifyDataSetChanged();
                bind.recyclerView.scrollToPosition(jokes.size() - 1);
            }
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
