package com.udacity.gradle.builditbigger.Search;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SubscribersSubsrciptions.SubsAdapter;
import com.udacity.gradle.builditbigger.databinding.FragmentSearchUserBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchUserFragment extends Fragment {
    //todo might want to order by follower count
    private List<HilarityUser> allUsers;
    private String startAt;
    private String query;
    public SearchUserFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchUserFragment.
     */
    public static SearchUserFragment newInstance() {
        return new SearchUserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentSearchUserBinding bind = DataBindingUtil.inflate(inflater, R.layout.fragment_search_user, container, false);
        SubsAdapter subsAdapter = new SubsAdapter(new ArrayList<>(), getActivity());
        bind.recyclerview.setAdapter(subsAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        bind.recyclerview.setLayoutManager(llm);
        bind.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (llm.findLastVisibleItemPosition() >= (allUsers.size() - 5)){
                    performQuery(false, subsAdapter, query);
                }
            }
        });

        SearchHilarityViewModel searchHilarityViewModel = ViewModelProviders.of(this, new SearchHilarityViewModelProvider()).get(SearchHilarityViewModel.class);
        searchHilarityViewModel.getSearchQuery().observe(this, query -> {
            performQuery(true, subsAdapter,query);
        });
        return bind.getRoot();
    }

    public void performQuery(boolean init, SubsAdapter subsAdapter, String query){
        if (init) this.query = query;
        CollectionReference firestore = Constants.FIRESTORE.collection("users");
        if (!init) firestore.startAfter(startAt);
        firestore.whereGreaterThanOrEqualTo("userName", query)
                .whereLessThanOrEqualTo("userName", query + "z")
                .limit(20).get()
                .addOnSuccessListener(documentSnapshots -> {
                    if (init) allUsers = new ArrayList<>();
                    for (DocumentSnapshot item : documentSnapshots.getDocuments()) {
                        allUsers.add(item.toObject(HilarityUser.class));
                        if (allUsers.size() % 20 == 0) {startAt = item.toObject(HilarityUser.class).getUserName();}
                    }
                    subsAdapter.setSubscribersList(allUsers);
                });
    }

}
