package com.udacity.gradle.builditbigger.Search;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentSearchVideoPostsBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchAudioPostsFragment extends Fragment {

    public static SearchAudioPostsFragment newInstance() {
        return new SearchAudioPostsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSearchVideoPostsBinding bind = DataBindingUtil.inflate(inflater, R.layout.fragment_search_video_posts, container, false);
        JokesAdapter jokesAdapter = new JokesAdapter(getActivity(), new ArrayList<>(), false);
        bind.recyclerview.setAdapter(jokesAdapter);
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ViewModelProviders.of(this).get(SearchHilarityViewModel.class).getSearchQuery().observe(this, query -> {
            Constants.FIRESTORE.collection("posts").whereEqualTo("metaData.keywords.audio", true)
                    .orderBy("timeStamp", Query.Direction.DESCENDING).get()
                    .addOnSuccessListener(documentSnapshots -> {
                        List<Post> videoPosts = new ArrayList<>();
                        for (DocumentSnapshot snap : documentSnapshots.getDocuments()) {
                            videoPosts.add(snap.toObject(Post.class));
                        }
                        jokesAdapter.setJokes(videoPosts);
                    });
        });
        return bind.getRoot();
    }
}
