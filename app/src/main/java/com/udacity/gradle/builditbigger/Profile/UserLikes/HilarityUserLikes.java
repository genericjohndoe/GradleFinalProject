package com.udacity.gradle.builditbigger.Profile.UserLikes;

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
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Interfaces.HideFAB;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Profile.UserPosts.HilarityUserJokes;
import com.udacity.gradle.builditbigger.Profile.UserPosts.SearchUserPostsViewModelFactory;
import com.udacity.gradle.builditbigger.Profile.UserPosts.SearchUserViewModel;
import com.udacity.gradle.builditbigger.Profile.UserPosts.UserPostViewModelFactory;
import com.udacity.gradle.builditbigger.Profile.UserPosts.UserPostsViewModel;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;
import com.udacity.gradle.builditbigger.Interfaces.VideoCallback;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * HilarityUserLikes class shows posts liked by the user
 */

public class HilarityUserLikes extends Fragment {
    //todo test search
    HideFAB conFam;
    JokesAdapter jokeAdapter;
    List<Joke> jokes = new ArrayList<>();
    FragmentJokeslistGenrelistBinding binding;
    private String uid;
    private boolean searched = false;

    public static HilarityUserLikes newInstance(String uid, HideFAB conFam){
        HilarityUserLikes hilarityUserLikes = new HilarityUserLikes();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        hilarityUserLikes.setArguments(bundle);
        hilarityUserLikes.conFam = conFam;
        return hilarityUserLikes;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getArguments().getString("uid");
        jokeAdapter = new JokesAdapter(getActivity(), jokes, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jokeslist_genrelist, container, false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        binding.recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
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
        //returns original list after search
        binding.recyclerView.setOnKeyListener((v, keyCode, event) -> {
            if(keyCode == KeyEvent.KEYCODE_BACK && searched){
                jokeAdapter = new JokesAdapter(getActivity(), jokes, false);
                jokeAdapter.notifyDataSetChanged();
                searched = false;
                configureUI();
                binding.recyclerView.scrollToPosition(jokes.size() - 1);
                return true;
            }
            return false;
        });
        UserLikesViewModel userLikesViewModel = ViewModelProviders.of(this, new UserLikesViewModelFactory(uid))
                .get(UserLikesViewModel.class);

        userLikesViewModel.getUserLikesLiveData().observe(this, joke -> {
            if (!jokes.contains(joke))jokes.add(joke);
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
                            jokeAdapter = new JokesAdapter(getActivity(),searches, false);
                    //go through jokes list, search metadata, if metadata contains search term add 2 new list
                    //then call setList
                        }
                )
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
