package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

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

import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * shows list of people followed by user
 */

public class SubscriptionsFragment extends Fragment {
    private String uid;

    public static SubscriptionsFragment newInstance(String uid){
        SubscriptionsFragment subscriptionsFragment = new SubscriptionsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        subscriptionsFragment.setArguments(bundle);
        return subscriptionsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getArguments().getString("uid");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentJokeslistGenrelistBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_jokeslist_genrelist, container, false);
        List<HilarityUser> subscriptions = new ArrayList<>();
        SubsAdapter subsAdapter = new SubsAdapter(subscriptions, getActivity());
        SubscriptionsViewModel subscriptionsViewModel = ViewModelProviders.of(this, new SubscriptionsViewModelFactory(uid))
                .get(SubscriptionsViewModel.class);
        SubscriptionsLiveData subscriptionsLiveData = subscriptionsViewModel.getSubscriptionsLiveData();
        subscriptionsLiveData.observe(this, hilarityUser -> {
            if (!subscriptions.contains(hilarityUser)) {
                subscriptions.add(hilarityUser);
                subsAdapter.notifyDataSetChanged();
                if (subscriptions.size() % 20 == 0) subscriptionsLiveData.setStartAt(hilarityUser.getUserName());
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setAdapter(subsAdapter);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (llm.findLastVisibleItemPosition() >= (subscriptions.size() - 5)) {
                    subscriptionsLiveData.newQuery();
                }
            }
        });

        return binding.getRoot();
    }
}
