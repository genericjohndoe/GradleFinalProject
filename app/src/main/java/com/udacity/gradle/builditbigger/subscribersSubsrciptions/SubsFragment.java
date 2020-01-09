package com.udacity.gradle.builditbigger.subscribersSubsrciptions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;
import com.udacity.gradle.builditbigger.models.HilarityUser;

import java.util.ArrayList;
import java.util.List;

public class SubsFragment extends Fragment {
    private String uid;
    private boolean getFollowers;

    public static SubsFragment newInstance(String uid, boolean getFollowers){
        SubsFragment subsFragment = new SubsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putBoolean("getFollowers", getFollowers);
        subsFragment.setArguments(bundle);
        return subsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString("uid");
            getFollowers = getArguments().getBoolean("getFollowers");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentJokeslistGenrelistBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jokeslist_genrelist, container, false);
        List<HilarityUser> subs = new ArrayList<>();
        SubsAdapter subsAdapter = new SubsAdapter(subs, getActivity());
        SubsViewModel subsViewModel = ViewModelProviders.of(this, new SubsViewModelFactory(uid, getFollowers))
                .get(SubsViewModel.class);
        SubsLiveData subsLiveData = subsViewModel.getSubsLiveData();
        subsLiveData.observe(this, hilarityUser -> {
            if (!subs.contains(hilarityUser)) {
                subs.add(hilarityUser);
                subsAdapter.notifyDataSetChanged();
                if (subs.size() % 20 == 0) subsLiveData.setStartAt(hilarityUser.getUserName());
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setAdapter(subsAdapter);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (llm.findLastVisibleItemPosition() >= (subs.size() - 5)) {
                    subsLiveData.newQuery();
                }
            }
        });

        return binding.getRoot();
    }
}
