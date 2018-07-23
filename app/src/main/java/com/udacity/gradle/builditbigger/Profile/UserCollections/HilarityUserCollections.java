package com.udacity.gradle.builditbigger.Profile.UserCollections;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Collections.CollectionAdapter;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Interfaces.EnableSearch;
import com.udacity.gradle.builditbigger.Interfaces.HideFAB;
import com.udacity.gradle.builditbigger.Models.Collection;
import com.udacity.gradle.builditbigger.Profile.FragmentFocusLiveData;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Search.SearchDialogFragment;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joeljohnson on 10/12/17.
 */

public class HilarityUserCollections extends Fragment implements EnableSearch {
    private CollectionAdapter collectionAdapter;
    private HideFAB profile;
    private List<Collection> collections;
    private FragmentJokeslistGenrelistBinding binding;
    private String uid;
    private UserCollectionViewModel userCollectionViewModel;

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
        collections = new ArrayList<>();
        if (getArguments() != null) uid = getArguments().getString(getString(R.string.uid));
        collectionAdapter = new CollectionAdapter(getActivity(), collections);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jokeslist_genrelist, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        llm.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setAdapter(collectionAdapter);

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

        userCollectionViewModel = ViewModelProviders.of(this,
                new UserCollectionViewModelFactory(uid))
                .get(UserCollectionViewModel.class);
        userCollectionViewModel.getUserCollectionLiveData().observe(this, genre -> {
            addCollectionToList(genre, collections);
            binding.recyclerView.scrollToPosition(collections.size() - 1);
            configureUI();
        });
        FragmentFocusLiveData.getFragmentFocusLiveData().observe(this, position ->{
            if (position == 1) profile.getFAB().setOnClickListener(view -> showSearchDialog());
        });
        return binding.getRoot();
    }

    public void showSearchDialog() {
        SearchDialogFragment.getInstance(this).show(getFragmentManager(), "search");
    }

    public void configureUI() {
        if (collections.isEmpty()) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.noItemImageview.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noItemImageview.setVisibility(View.GONE);
        }
    }

    public void configureFAM() {
        profile.getFAM().setVisibility(View.VISIBLE);
        profile.getFAM().setOnMenuButtonClickListener(view -> {
            collectionAdapter.setCollections(collections);
            profile.getFAM().setVisibility(View.GONE);
        });
    }

    private void addCollectionToList(Collection collection, List<Collection> collectionList){
        if (!collectionAdapter.getCollections().equals(collectionList))
            collectionAdapter.setCollections(collectionList);
        if (!collectionList.contains(collection) && (uid.equals(Constants.UID) || !collection.getRestricted())) {
            collectionList.add(collection);
            collectionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void search(String keyword) {
        List<Collection> filteredSearch = new ArrayList<>();
        userCollectionViewModel.getSearchUserCollectionLiveData(keyword).observe(this, collection -> {
            addCollectionToList(collection,filteredSearch);
            configureFAM();
        });
    }
}
