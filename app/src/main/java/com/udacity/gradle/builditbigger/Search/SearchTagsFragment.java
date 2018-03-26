package com.udacity.gradle.builditbigger.Search;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentSearchTagsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchTagsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchTagsFragment extends Fragment {

    public SearchTagsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SearchTagsFragment.
     */
    public static SearchTagsFragment newInstance() {
        return new SearchTagsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSearchTagsBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_search_tags, container, false);
        JokesAdapter jokesAdapter = new JokesAdapter(getActivity(), new ArrayList<>(), false);
        bind.recyclerview.setAdapter(jokesAdapter);
        ViewModelProviders.of(this, new SearchHilarityViewModelProvider()).get(SearchHilarityViewModel.class).getSearchQuery().observe(this, query ->{
            Constants.FIRESTORE.collection("posts").whereGreaterThanOrEqualTo("metaData.tags."+query, true).get().addOnSuccessListener(documentSnapshots -> {
                List<Joke> tags = new ArrayList<>();
                for (DocumentSnapshot snap: documentSnapshots.getDocuments()){
                    tags.add(snap.toObject(Joke.class));
                }
                jokesAdapter.setJokes(tags);
            });
        });
        return bind.getRoot();
    }

}
