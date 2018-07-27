package com.udacity.gradle.builditbigger.Search;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.udacity.gradle.builditbigger.Collections.CollectionAdapter;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Collection;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentSearchCollectionsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchCollectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchCollectionsFragment extends Fragment {
    private long startAt;
    private String query;
    private List<Collection> genres;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSearchCollectionsBinding bind = DataBindingUtil.inflate(inflater, R.layout.fragment_search_collections, container, false);
        CollectionAdapter genreAdapter = new CollectionAdapter(getActivity(), new ArrayList<>());
        bind.recyclerview.setAdapter(genreAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        bind.recyclerview.setLayoutManager(llm);
        bind.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (llm.findLastVisibleItemPosition() >= (genres.size() - 5)){
                    performQuery(false, genreAdapter, query);
                }
            }
        });
        ViewModelProviders.of(this, new SearchHilarityViewModelProvider()).get(SearchHilarityViewModel.class).getSearchQuery().observe(this, query -> {
            performQuery(true, genreAdapter, query);
        });
        return bind.getRoot();
    }

    public void performQuery(boolean init, CollectionAdapter genreAdapter, String query){
        if (init) this.query = query;
        CollectionReference firestore = Constants.FIRESTORE.collection("collections");
        if (!init) firestore.startAfter(startAt);
        firestore.whereGreaterThanOrEqualTo("title", query)
                .whereLessThanOrEqualTo("title", query+"z")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .limit(20).get()
                .addOnSuccessListener(documentSnapshots-> {
                            if (init) genres = new ArrayList<>();
                            for (DocumentSnapshot snap : documentSnapshots.getDocuments()) {
                                genres.add(snap.toObject(Collection.class));
                                if (genres.size() % 20 == 0) startAt = snap.toObject(Collection.class).getTimeStamp();
                            }
                            genreAdapter.setCollections(genres);
                        }
                );
    }
}
