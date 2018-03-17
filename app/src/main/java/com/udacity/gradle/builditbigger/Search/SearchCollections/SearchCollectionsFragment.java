package com.udacity.gradle.builditbigger.Search.SearchCollections;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Models.Genre;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Search.SearchHilarityViewModel;
import com.udacity.gradle.builditbigger.databinding.FragmentSearchCollectionsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchCollectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchCollectionsFragment extends Fragment {
    public SearchCollectionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SearchCollectionsFragment.
     */
    public static SearchCollectionsFragment newInstance() {
        return new SearchCollectionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSearchCollectionsBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_search_collections, container, false);
        List<Genre> genres = new ArrayList<>();
        ViewModelProviders.of(this).get(SearchHilarityViewModel.class).getSearchCollectionsLiveData().observe(this, collection ->{
            genres.add(collection);
        });
        return bind.getRoot();
    }
}
