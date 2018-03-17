package com.udacity.gradle.builditbigger.Search.SearchTags;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Search.SearchHilarityViewModel;
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
        // Inflate the layout for this fragment
        FragmentSearchTagsBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_search_tags, container, false);
        List<String> tags = new ArrayList<>();
        ViewModelProviders.of(this).get(SearchHilarityViewModel.class).getSearchTagsLiveData().observe(this, tag -> {
            tags.add(tag);
        });
        return bind.getRoot();
    }

}
