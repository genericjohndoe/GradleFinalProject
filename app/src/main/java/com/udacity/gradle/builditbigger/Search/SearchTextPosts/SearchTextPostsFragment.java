package com.udacity.gradle.builditbigger.Search.SearchTextPosts;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Search.SearchHilarityViewModel;
import com.udacity.gradle.builditbigger.databinding.FragmentSearchTextPostsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchTextPostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchTextPostsFragment extends Fragment {

    public SearchTextPostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SearchTextPostsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchTextPostsFragment newInstance() {
        return new SearchTextPostsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSearchTextPostsBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_search_text_posts, container, false);
        List<Joke> textPosts = new ArrayList<>();
        ViewModelProviders.of(this).get(SearchHilarityViewModel.class).getSearchTextPostsLiveData().observe(this, post ->{
            textPosts.add(post);
        });
        return bind.getRoot();
    }

}
