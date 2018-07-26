package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * shows list of subscribers for given user
 */

public class SubscribersFragment extends Fragment {
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentJokeslistGenrelistBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jokeslist_genrelist, container, false);
        List<HilarityUser> subscribers = new ArrayList<>();
        SubsAdapter subsAdapter = new SubsAdapter(subscribers, getActivity());
        SubscribersViewModel subscribersViewModel = ViewModelProviders.of(this,
                new SubscribersViewModelFactory(uid))
                .get(SubscribersViewModel.class);

        SubscribersLiveData subscribersLiveData = subscribersViewModel.getSubscribersLiveData();
        subscribersLiveData.observe(this, hilarityUser -> {
            if (!subscribers.contains(hilarityUser)) {
                subscribers.add(hilarityUser);
                subsAdapter.notifyDataSetChanged();
                if (subscribers.size() % 20 == 0) subscribersLiveData.setStartAt(hilarityUser.getUserName());
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setAdapter(subsAdapter);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (llm.findLastVisibleItemPosition() >= (subscribers.size() - 5)) {
                    subscribersLiveData.newQuery();
                }
            }
        });


        return binding.getRoot();
    }
}
