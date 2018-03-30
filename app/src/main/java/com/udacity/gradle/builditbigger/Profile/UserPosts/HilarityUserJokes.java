package com.udacity.gradle.builditbigger.Profile.UserPosts;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Interfaces.HideFAB;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Jokes.SwipeToDeleteCallback;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * HilarityUserJokes class shows user generated posts
 */

public class HilarityUserJokes extends Fragment {
    //todo test search and back press
    //todo allow for delete by swipe
    JokesAdapter jokeAdapter;
    List<Joke> jokes;
    HideFAB conFam;
    private FragmentJokeslistGenrelistBinding binding;
    private String uid;
    private boolean searched = false;

    public static HilarityUserJokes newInstance(String uid, HideFAB conFam) {
        HilarityUserJokes hilarityUserJokes = new HilarityUserJokes();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        hilarityUserJokes.setArguments(bundle);
        hilarityUserJokes.conFam = conFam;
        return hilarityUserJokes;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getArguments().getString("uid");
        jokes = new ArrayList<>();
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
                if (dy > 0 || dy < 0) conFam.hideFAB();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) conFam.showFAB();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        //returns full list of posts after search
        binding.recyclerView.setOnKeyListener((v, keyCode, event) -> {
            if(keyCode == KeyEvent.KEYCODE_BACK && searched){
                jokeAdapter.setJokes(jokes);
                searched = false;
                configureUI();
                binding.recyclerView.scrollToPosition(jokes.size() - 1);
                return true;
            }
            return false;
        });
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Joke joke = ((JokesAdapter.JokesViewHolder) viewHolder).getJoke();
                Constants.DATABASE.child("/userposts/"+Constants.UID+"/posts/"+joke.getPushId()).removeValue((databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        jokes.remove(joke);
                        jokeAdapter.notifyDataSetChanged();
                        Log.i("hilaritydelete", "removed");
                    }
                });
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);

        /*ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.START, ItemTouchHelper.START) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Joke joke = ((JokesAdapter.JokesViewHolder) viewHolder).getJoke();
                Constants.DATABASE.child("/userposts/"+Constants.UID+"/posts/"+joke.getPushId()).removeValue((databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        jokes.remove(joke);
                        jokeAdapter.notifyDataSetChanged();
                        Log.i("hilaritydelete", "removed");
                    }
                });
            }
        });*/
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);

        UserPostsViewModel userPostsViewModel = ViewModelProviders.of(this,
                new UserPostViewModelFactory(uid))
                .get(UserPostsViewModel.class);
        userPostsViewModel.getUserPostsLiveData().observe(this, joke -> {

            if (!jokes.contains(joke)) jokes.add(joke);

            if (!searched) {
                jokeAdapter.notifyDataSetChanged();
                configureUI();
                binding.recyclerView.scrollToPosition(jokes.size() - 1);
            }
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
                            //go through jokes list, search metadata, if metadata contains search term add 2 new list
                            //then call setList
                        })
                .onNegative((dialog, which) -> dialog.dismiss())
                .show().setCanceledOnTouchOutside(false);
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
}
