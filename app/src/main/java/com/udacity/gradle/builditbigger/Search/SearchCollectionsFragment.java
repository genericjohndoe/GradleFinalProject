package com.udacity.gradle.builditbigger.Search;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Genres.GenreAdapter;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentSearchCollectionsBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchCollectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchCollectionsFragment extends Fragment {
    public SearchCollectionsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
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
        FragmentSearchCollectionsBinding bind = DataBindingUtil.inflate(inflater, R.layout.fragment_search_collections, container, false);
        GenreAdapter genreAdapter = new GenreAdapter(getActivity(), new ArrayList<>());
        bind.recyclerview.setAdapter(genreAdapter);
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ViewModelProviders.of(this, new SearchHilarityViewModelProvider()).get(SearchHilarityViewModel.class).getSearchQuery().observe(this, query -> {
            Constants.FIRESTORE.collection("collections").whereGreaterThanOrEqualTo("title", query).get().addOnSuccessListener(documentSnapshots-> {
                    List<com.udacity.gradle.builditbigger.Models.Collection> genres = new ArrayList<>();
                    for (DocumentSnapshot snap : documentSnapshots.getDocuments()) {
                        genres.add(snap.toObject(com.udacity.gradle.builditbigger.Models.Collection.class));
                    }
                    genreAdapter.setGenres(genres);
                }
            );
        });
        return bind.getRoot();
    }
}
