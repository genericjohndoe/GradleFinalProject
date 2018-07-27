package com.udacity.gradle.builditbigger.Search;

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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentSearchTagsBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchPost extends Fragment {
    private String path;
    private String query;
    private List<Post> posts;
    private long startAfter;


    public static SearchPost newInstance(String path){
        SearchPost searchPost = new SearchPost();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        searchPost.setArguments(bundle);
        return searchPost;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) path = getArguments().getString("path");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSearchTagsBinding bind = DataBindingUtil.inflate(inflater, R.layout.fragment_search_tags, container, false);
        JokesAdapter jokesAdapter = new JokesAdapter(getActivity(), new ArrayList<>(), false);
        bind.recyclerview.setAdapter(jokesAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        bind.recyclerview.setLayoutManager(llm);
        bind.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (llm.findLastVisibleItemPosition() >= (posts.size() - 5)) {
                    performQuery(false, jokesAdapter, query);
                }
            }
        });
        ViewModelProviders.of(this, new SearchHilarityViewModelProvider())
                .get(SearchHilarityViewModel.class).getSearchQuery().observe(this, query -> {
            performQuery(true, jokesAdapter, query);
        });
        return bind.getRoot();
    }

    public void performQuery(boolean init, JokesAdapter jokesAdapter, String query){
        if (init) this.query = query;
        CollectionReference firestore = Constants.FIRESTORE.collection("posts");
        if (!init) firestore.startAfter(startAfter);
        if (path != null) firestore.whereEqualTo(path, true);
        firestore.whereEqualTo("metaData." + query, true)
                .orderBy("timeStamp", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (init) posts = new ArrayList<>();
                    for (DocumentSnapshot snap : documentSnapshots.getDocuments()) {
                        posts.add(snap.toObject(Post.class));
                        if (posts.size() % 20 == 0) startAfter = snap.toObject(Post.class).getTimeStamp();
                    }
                    jokesAdapter.setJokes(posts);
                });
    }
}
