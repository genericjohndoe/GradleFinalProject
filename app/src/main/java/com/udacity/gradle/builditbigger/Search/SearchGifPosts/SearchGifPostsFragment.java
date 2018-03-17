package com.udacity.gradle.builditbigger.Search.SearchGifPosts;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentSearchGifPostsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchGifPostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchGifPostsFragment extends Fragment {


    public SearchGifPostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SearchGifPostsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchGifPostsFragment newInstance() {
        return new SearchGifPostsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSearchGifPostsBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_search_gif_posts, container, false);
        return bind.getRoot();
    }

}
