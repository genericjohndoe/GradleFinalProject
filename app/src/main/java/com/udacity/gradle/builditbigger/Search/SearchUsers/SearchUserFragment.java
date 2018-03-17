package com.udacity.gradle.builditbigger.Search.SearchUsers;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Search.SearchHilarityViewModel;
import com.udacity.gradle.builditbigger.databinding.FragmentSearchUserBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchUserFragment extends Fragment {

    public SearchUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SearchUserFragment.
     */
    public static SearchUserFragment newInstance() {
        SearchUserFragment fragment = new SearchUserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSearchUserBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_search_user, container, false);
        List<HilarityUser> allUsers = new ArrayList<>();
        ViewModelProviders.of(this).get(SearchHilarityViewModel.class).getSearchUserLiveData().observe(this, user -> {
            allUsers.add(user);
        });
        return bind.getRoot();
    }

}
