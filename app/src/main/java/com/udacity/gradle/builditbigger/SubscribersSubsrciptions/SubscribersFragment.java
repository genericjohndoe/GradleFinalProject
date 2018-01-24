package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.Profile.UserPosts.UserPostViewModelFactory;
import com.udacity.gradle.builditbigger.Profile.UserPosts.UserPostsViewModel;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joeljohnson on 11/27/17.
 */

public class SubscribersFragment extends Fragment {
    //todo if button in cell and cell have different on clicks, which is registered first does clicking in different
    //todo provide the desired effect
    private List<HilarityUser> subscribers;
    private String uid;


    public static SubscribersFragment newInstance(String uid){
        SubscribersFragment subscribersFragment = new SubscribersFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        subscribersFragment.setArguments(bundle);
        return subscribersFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getArguments().getString("uid");
        subscribers = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentJokeslistGenrelistBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jokeslist_genrelist, container, false);
        SubsAdapter subsAdapter = new SubsAdapter(subscribers, getActivity());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(subsAdapter);

        SubscribersViewModel subscribersViewModel = ViewModelProviders.of(this,
                new SubscribersViewModelFactory(uid))
                .get(SubscribersViewModel.class);

        subscribersViewModel.getSubscribersLiveData().observe(this, hilarityUser -> {
            subscribers.add(hilarityUser);
            subsAdapter.notifyDataSetChanged();
        });
        return binding.getRoot();
    }
}
