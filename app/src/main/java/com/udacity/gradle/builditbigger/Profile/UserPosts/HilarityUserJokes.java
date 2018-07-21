package com.udacity.gradle.builditbigger.Profile.UserPosts;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Interfaces.EnableSearch;
import com.udacity.gradle.builditbigger.Interfaces.HideFAB;
import com.udacity.gradle.builditbigger.Interfaces.VideoInfoTransfer;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.Models.PostWrapper;
import com.udacity.gradle.builditbigger.Models.VideoInfo;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Search.SearchDialogFragment;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * HilarityUserJokes class shows user generated posts
 */

public class HilarityUserJokes extends Fragment implements EnableSearch {
    //todo test search and back press
    HideFAB profile;
    JokesAdapter jokeAdapter;
    List<Post> jokes = new ArrayList<>();
    private FragmentJokeslistGenrelistBinding binding;
    private String uid;
    private boolean searched = false;
    UserPostsViewModel userPostsViewModel;

    public static HilarityUserJokes newInstance(String uid, HideFAB profile) {
        HilarityUserJokes hilarityUserJokes = new HilarityUserJokes();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        hilarityUserJokes.setArguments(bundle);
        hilarityUserJokes.profile = profile;
        return hilarityUserJokes;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(getString(R.string.uid));
        }
        if (savedInstanceState != null)
            jokes = savedInstanceState.getParcelableArrayList(getString(R.string.posts));
        jokeAdapter = new JokesAdapter(getActivity(), jokes, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jokeslist_genrelist, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        llm.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setAdapter(jokeAdapter);

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0) profile.hideFAB();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) profile.showFAB();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        userPostsViewModel = ViewModelProviders.of(this,
                new UserPostViewModelFactory(uid))
                .get(UserPostsViewModel.class);
        userPostsViewModel.getUserPostsLiveData().observe(this, postWrapper -> {
            addPostToList(postWrapper, jokes);
        });

        configureUI();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()) profile.getFAB().setOnClickListener(view -> showSearchDialog());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.posts), (ArrayList<? extends Parcelable>) jokes);
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
            jokes.remove(post);
            jokes.add(index, post);
            jokeAdapter.notifyDataSetChanged();
        }
        if (!searched) {
            jokeAdapter.notifyDataSetChanged();
            configureUI();
            binding.recyclerView.scrollToPosition(jokes.size() - 1);
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
        Log.i("HUJ", keyword);
        List<Post> searchedPosts = new ArrayList<>();
        userPostsViewModel.getSearchUserPostsLiveData(keyword).observe(this, postWrapper -> {
            addPostToList(postWrapper, searchedPosts);
            configureFAM();
        });
    }
}
