package com.udacity.gradle.builditbigger.Profile.UserGenres;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Models.Genre;
import com.udacity.gradle.builditbigger.Genres.GenreAdapter;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.Profile.UserInfoViewModel;
import com.udacity.gradle.builditbigger.Profile.UserInfoViewModelFactory;
import com.udacity.gradle.builditbigger.Profile.UserPosts.HilarityUserJokes;
import com.udacity.gradle.builditbigger.Profile.UserPosts.SearchUserPostsViewModelFactory;
import com.udacity.gradle.builditbigger.Profile.UserPosts.SearchUserViewModel;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joeljohnson on 10/12/17.
 */

public class HilarityUserGenres extends Fragment {
    //todo test search
    GenreAdapter genreAdapter;
    List<Genre> genres;
    private FragmentJokeslistGenrelistBinding binding;
    private String uid;
    private HideFAB conFam;
    private boolean searched = false;

    public static HilarityUserGenres newInstance(String uid) {
        HilarityUserGenres hilarityUserGenres = new HilarityUserGenres();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        hilarityUserGenres.setArguments(bundle);
        return hilarityUserGenres;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        genres = new ArrayList<>();
        uid = getArguments().getString("uid");
        genreAdapter = new GenreAdapter(getActivity(), genres);
        conFam = (HideFAB) getActivity().getSupportFragmentManager().findFragmentByTag("profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jokeslist_genrelist, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        llm.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        binding.recyclerView.setAdapter(genreAdapter);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0) {
                    conFam.hideFAB();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    conFam.showFAB();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        UserGenreViewModel userGenreViewModel = ViewModelProviders.of(this,
                new UserGenreViewModelFactory(uid))
                .get(UserGenreViewModel.class);
        userGenreViewModel.getUserGenreLiveData().observe(this, dataSnapshot -> {
            Genre genre = dataSnapshot.getValue(Genre.class);
            genres.add(genre);
            if (!searched) {
                genreAdapter.notifyDataSetChanged();
                configureUI();
                if (binding.recyclerView != null)
                    binding.recyclerView.scrollToPosition(genres.size() - 1);
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
                            List<Genre> searches = new ArrayList<>();
                            genreAdapter = new GenreAdapter(getActivity(),searches);
                            ViewModelProviders.of(this,
                                    new SearchUserGenreViewModelFactory(uid, splitSearchKeyword))
                                    .get(SearchUserGenreViewModel.class).getSearchUserGenreLiveData()
                                    .observe(this, dataSnapshot -> {
                                        Genre genre = dataSnapshot.getValue(Genre.class);
                                        if (!searches.contains(genre)) searches.add(genre);
                                        genreAdapter.notifyDataSetChanged();
                                        configureUI();
                                        binding.recyclerView.scrollToPosition(searches.size() - 1);
                                        binding.recyclerView.requestFocus();
                                    });
                        }
                )
                .onNegative((dialog, which) -> dialog.dismiss())
                .show().setCanceledOnTouchOutside(false);
    }

    public void configureUI() {
        if (genres.isEmpty()) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.noItemImageview.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noItemImageview.setVisibility(View.GONE);
        }
    }
}
