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
import com.udacity.gradle.builditbigger.databinding.FragmentSearchImagePostsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchImagePostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchImagePostsFragment extends Fragment {

    public SearchImagePostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SearchImagePostsFragment.
     */
    public static SearchImagePostsFragment newInstance() {
        return new SearchImagePostsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSearchImagePostsBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_search_image_posts, container, false);
        JokesAdapter jokesAdapter = new JokesAdapter(getActivity(), new ArrayList<>(), false);
        bind.recyclerview.setAdapter(jokesAdapter);
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        ViewModelProviders.of(this).get(SearchHilarityViewModel.class).getSearchQuery().observe(this, query -> {
            Constants.FIRESTORE.collection("posts").whereEqualTo("metaData.keywords.image", true)
                    .orderBy("timeStamp", Query.Direction.DESCENDING).get()
                    .addOnSuccessListener(documentSnapshots -> {
                        List<Post> imagePosts = new ArrayList<>();
                        for (DocumentSnapshot snap : documentSnapshots.getDocuments()) {
                            imagePosts.add(snap.toObject(Post.class));
                        }
                        jokesAdapter.setJokes(imagePosts);
                    });
        });
        return bind.getRoot();
    }

}
