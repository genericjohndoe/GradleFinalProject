package com.udacity.gradle.builditbigger.Profile.UserCollections;

import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.ActivityInfo;
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
import com.udacity.gradle.builditbigger.Collections.CollectionAdapter;
import com.udacity.gradle.builditbigger.Interfaces.HideFAB;
import com.udacity.gradle.builditbigger.Models.Collection;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SimpleDividerItemDecoration;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joeljohnson on 10/12/17.
 */

public class HilarityUserCollections extends Fragment {
    //todo test search
    CollectionAdapter genreAdapter;
    HideFAB profile;
    List<Collection> genres;
    private FragmentJokeslistGenrelistBinding binding;
    private String uid;
    private boolean searched = false;

    public static HilarityUserCollections newInstance(String uid, HideFAB profile) {
        HilarityUserCollections hilarityUserGenres = new HilarityUserCollections();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        hilarityUserGenres.setArguments(bundle);
        hilarityUserGenres.profile = profile;
        return hilarityUserGenres;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        genres = new ArrayList<>();
        if (getArguments() != null) uid = getArguments().getString(getString(R.string.uid));
        genreAdapter = new CollectionAdapter(getActivity(), genres);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jokeslist_genrelist, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        llm.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setAdapter(genreAdapter);

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

        UserCollectionViewModel userCollectionViewModel = ViewModelProviders.of(this,
                new UserCollectionViewModelFactory(uid))
                .get(UserCollectionViewModel.class);
        userCollectionViewModel.getUserCollectionLiveData().observe(this, genre -> {
            if (!genres.contains(genre))genres.add(genre);
            if (!searched) {
                genreAdapter.notifyDataSetChanged();
                configureUI();
                binding.recyclerView.scrollToPosition(genres.size() - 1);
            }
        });
        configureUI();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (isVisible()) profile.getFAB().setOnClickListener(view -> showSearchDialog());
    }

    public void showSearchDialog() {
        new MaterialDialog.Builder(getActivity())
                .customView(R.layout.search, true)
                .positiveText(R.string.search)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> {
                            searched = true;
                            View view2 = dialog.getCustomView();
                            String searchKeyword = ((EditText) view2.findViewById(R.id.search)).getText().toString();
                            List<Collection> searches = new ArrayList<>();
                            for (Collection collection: genres){
                                if (collection.getTitle().contains(searchKeyword)) searches.add(collection);
                            }
                            genreAdapter.setGenres(searches);
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
