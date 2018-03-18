package com.udacity.gradle.builditbigger.Search;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Tags.TagAdapter;
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
        TagAdapter tagAdapter = new TagAdapter(new ArrayList<>(), getActivity());
        ViewModelProviders.of(this, new SearchHilarityViewModelProvider()).get(SearchHilarityViewModel.class).getSearchQuery().observe(this, query ->{
            Constants.DATABASE.child("taglist").orderByKey().startAt(query).endAt(query+"\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> tags = new ArrayList<>();
                    for (DataSnapshot snap: dataSnapshot.getChildren()){
                        tags.add(snap.getValue(String.class));
                    }
                    tagAdapter.setTags(tags);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        });
        return bind.getRoot();
    }

}
