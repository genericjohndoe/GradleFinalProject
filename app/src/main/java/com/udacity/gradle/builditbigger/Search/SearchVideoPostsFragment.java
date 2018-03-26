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
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentSearchVideoPostsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchVideoPostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchVideoPostsFragment extends Fragment {

    public SearchVideoPostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SearchVideoPostsFragment.
     */
    public static SearchVideoPostsFragment newInstance() {
        return new SearchVideoPostsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSearchVideoPostsBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_search_video_posts, container, false);
        JokesAdapter jokesAdapter = new JokesAdapter(getActivity(), new ArrayList<>(), false);
        bind.recyclerview.setAdapter(jokesAdapter);
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        ViewModelProviders.of(this).get(SearchHilarityViewModel.class).getSearchQuery().observe(this, query -> {
            Constants.FIRESTORE.collection("posts").whereEqualTo("type", Constants.VIDEO).whereGreaterThanOrEqualTo("metaData.tags."+query, true).get()
                    .addOnSuccessListener(documentSnapshots -> {
                        List<Joke> videoPosts = new ArrayList<>();
                        for (DocumentSnapshot snap : documentSnapshots.getDocuments()) {
                            videoPosts.add(snap.toObject(Joke.class));
                        }
                        jokesAdapter.setJokes(videoPosts);
                    });
        });
        return bind.getRoot();
    }

}