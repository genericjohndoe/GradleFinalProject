package com.udacity.gradle.builditbigger.Profile.UserPosts;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.udacity.gradle.builditbigger.Interfaces.HideFAB;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Interfaces.VideoCallback;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joeljohnson on 10/12/17.
 */

public class HilarityUserJokes extends Fragment implements VideoCallback {
    //todo test search and back press
    List<Long> videosOnScreen = new ArrayList<>();
    long currentlyPlaying;
    JokesAdapter jokeAdapter;
    List<Joke> jokes;
    HideFAB conFam;
    private FragmentJokeslistGenrelistBinding binding;
    private String uid;
    private boolean searched = false;

    public static HilarityUserJokes newInstance(String uid) {
        HilarityUserJokes hilarityUserJokes = new HilarityUserJokes();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        hilarityUserJokes.setArguments(bundle);
        return hilarityUserJokes;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getArguments().getString("uid");
        jokes = new ArrayList<>();
        jokeAdapter = new JokesAdapter(getActivity(), jokes, this, true);
        conFam = (HideFAB) getActivity().getSupportFragmentManager().findFragmentByTag("profile");
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
                if (dy > 0 || dy < 0) conFam.hideFAB();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) conFam.showFAB();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        binding.recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {}

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Log.i("Hoe8", "child detached");
                if (view.findViewById(R.id.post_videoView) != null && ((SimpleExoPlayerView) view.findViewById(R.id.post_videoView)).getPlayer() != null) {
                    ((SimpleExoPlayerView) view.findViewById(R.id.post_videoView)).getPlayer().stop();
                }
            }
        });
        binding.recyclerView.setOnKeyListener((v, keyCode, event) -> {
            if(keyCode == KeyEvent.KEYCODE_BACK && searched){
                jokeAdapter = new JokesAdapter(getActivity(), jokes, this, true);
                jokeAdapter.notifyDataSetChanged();
                searched = false;
                configureUI();
                binding.recyclerView.scrollToPosition(jokes.size() - 1);
                return true;
            }
            return false;
        });

        UserPostsViewModel userPostsViewModel = ViewModelProviders.of(this,
                new UserPostViewModelFactory(uid))
                .get(UserPostsViewModel.class);
        userPostsViewModel.getUserPostsLiveData().observe(this, joke -> {
            //todo find way to keep the livedata from firing when it doesn't need to, find firebase article
            //when switching back and forth between fragments in viewpager
            //the last joke is readded to the list, only on client side
            if (!jokes.contains(joke)) {
                jokes.add(joke);
                Log.i("joke added", ""+joke.getPushId());
            }
            if (!searched) {
                jokeAdapter.notifyDataSetChanged();
                configureUI();
                binding.recyclerView.scrollToPosition(jokes.size() - 1);
            }
            Log.i("joke size", jokes.size() + "");
        });

        FloatingActionButton fab = conFam.getFAB();
        fab.setOnClickListener(view -> showSearchDialog());

        configureUI();
        return binding.getRoot();
    }

    //todo place in user jokes fragment
    public void showSearchDialog() {
        new MaterialDialog.Builder(getActivity())
                .customView(R.layout.search, true)
                .positiveText("Search")
                .negativeText("Cancel")
                .onPositive((dialog, which) -> {
                            searched = true;
                            View view2 = dialog.getCustomView();
                            String searchKeyword = ((EditText) view2.findViewById(R.id.search)).getText().toString();
                            String[] splitSearchKeyword = searchKeyword.split(" |\\,");
                            List<Joke> searches = new ArrayList<>();
                            jokeAdapter = new JokesAdapter(getActivity(),searches, HilarityUserJokes.this, true);
                            ViewModelProviders.of(this,
                                    new SearchUserPostsViewModelFactory(uid, splitSearchKeyword))
                                    .get(SearchUserViewModel.class).getSearchUserPostsLiveData()
                                    .observe(this, joke -> {
                                        if (!searches.contains(joke))
                                        searches.add(joke);
                                        jokeAdapter.notifyDataSetChanged();
                                        configureUI();
                                        binding.recyclerView.scrollToPosition(searches.size() - 1);
                                        binding.recyclerView.requestFocus();
                            });
                        })
                .onNegative((dialog, which) -> dialog.dismiss())
                .show().setCanceledOnTouchOutside(false);
    }

    public void configureUI() {
        if (jokes.isEmpty()) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.noItemImageview.setVisibility(View.VISIBLE);
            //searchEditText.setVisibility(View.GONE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noItemImageview.setVisibility(View.GONE);
            //searchEditText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getVideoInfo(boolean started, int position) {}

    @Override
    public void onNewVideoPost(long id) {
        videosOnScreen.add(id);
    }

    @Override
    public void onVideoPostRecycled(long id) {
        videosOnScreen.remove(Long.valueOf(id));
    }

    @Override
    public void setCurrentlyPlaying(long id) {
        Log.i("Hoe8", "video id before " + currentlyPlaying);
        Log.i("Hoe8", "video id parameter " + id);
        if (currentlyPlaying == 0) {
            currentlyPlaying = id;
            Log.i("Hoe8", "video 1 id after " + currentlyPlaying);
        } else if (currentlyPlaying != id) {
            //JokesAdapter.VideoPostViewHolder holder = (JokesAdapter.VideoPostViewHolder) binding.recyclerView.findViewHolderForItemId((long) currentlyPlaying);
            /*if (holder != null) {
                holder.getPost().getPlayer().stop();
            } else {
                Log.i("Hoe8", "holder is null");
            }*/
            currentlyPlaying = id;
            Log.i("Hoe8", "video 2 id after " + currentlyPlaying);
        }
    }
}
