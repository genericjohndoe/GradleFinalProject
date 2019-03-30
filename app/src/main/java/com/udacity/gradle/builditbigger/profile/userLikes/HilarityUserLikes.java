package com.udacity.gradle.builditbigger.profile.userLikes;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.interfaces.EnableSearch;
import com.udacity.gradle.builditbigger.interfaces.HideFAB;
import com.udacity.gradle.builditbigger.jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.models.Post;
import com.udacity.gradle.builditbigger.models.PostWrapper;
import com.udacity.gradle.builditbigger.profile.FragmentFocusLiveData;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.search.SearchDialogFragment;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * HilarityUserLikes class shows posts liked by the user
 */

public class HilarityUserLikes extends Fragment implements EnableSearch {
    //todo test search
    private HideFAB profile;
    private JokesAdapter jokeAdapter;
    private List<Post> jokes = new ArrayList<>();
    private FragmentJokeslistGenrelistBinding binding;
    private String uid;
    private UserLikesViewModel userLikesViewModel;

    public static HilarityUserLikes newInstance(String uid, HideFAB profile){
        HilarityUserLikes hilarityUserLikes = new HilarityUserLikes();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        hilarityUserLikes.setArguments(bundle);
        hilarityUserLikes.profile = profile;
        return hilarityUserLikes;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getArguments().getString(getString(R.string.uid));
        if (savedInstanceState != null) jokes = savedInstanceState.getParcelableArrayList(getString(R.string.posts));
        jokeAdapter = new JokesAdapter(getActivity(), jokes, false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jokeslist_genrelist, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        userLikesViewModel = ViewModelProviders.of(this, new UserLikesViewModelFactory(uid))
                .get(UserLikesViewModel.class);
        UserLikesLiveData userLikesLiveData = userLikesViewModel.getUserLikesLiveData();
        userLikesLiveData.observe(this, jokeWrapper -> {
            addPostToList(jokeWrapper, jokes);
            configureUI();
            if (jokes.size() % 20 == 0) {
                Log.i("new_query","startAt set");
                userLikesLiveData.setStartAt(jokeWrapper.getPost().getInverseTimeStamp());
            }
        });
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        binding.recyclerView.setAdapter(jokeAdapter);

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0) profile.hideFAB();
                if (llm.findLastVisibleItemPosition() >= (jokes.size() - 5)) {
                    userLikesLiveData.newQuery();
                    Log.i("new_query","new query called from fragment");
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) profile.showFAB();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });



        FragmentFocusLiveData.getFragmentFocusLiveData().observe(this, position ->{
            if (position == 2) profile.getFAB().setOnClickListener(view -> showSearchDialog());
        });
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.posts),(ArrayList<? extends Parcelable>) jokes);
    }


    public void showSearchDialog() {
        SearchDialogFragment.getInstance(this).show(getFragmentManager(), getString(R.string.search));
    }

    public void configureUI() {
        if (jokes.isEmpty()) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.noItemImageview.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noItemImageview.setVisibility(View.GONE);
        }
    }

    public void addPostToList(PostWrapper postWrapper, List<Post> jokes) {
        if (!jokeAdapter.getJokes().equals(jokes)) jokeAdapter.setJokes(jokes);
        Post post = postWrapper.getPost();
        if (!jokes.contains(post)) {
            jokes.add(post);
            jokeAdapter.notifyDataSetChanged();
        } else if (postWrapper.getState() == 2) {
            //if post gets modified
            int index = jokes.indexOf(post);
            jokes.set(index, post);
            jokeAdapter.notifyDataSetChanged();
        }
    }

    public void configureFAM() {
        profile.getFAM().setVisibility(View.VISIBLE);
        profile.getFAM().setOnMenuButtonClickListener(view -> {
            jokeAdapter.setJokes(jokes);
            profile.getFAM().setVisibility(View.GONE);
        });
    }

    @Override
    public void search(String keyword) {
        List<Post> searchedPosts = new ArrayList<>();
        userLikesViewModel.getSearchUserLikesLiveData(keyword).observe(this, postWrapper -> {
            addPostToList(postWrapper, searchedPosts);
            configureFAM();
        });
    }
}