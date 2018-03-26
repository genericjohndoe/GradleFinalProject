package com.udacity.gradle.builditbigger.Tags;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentTaggedJokesBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaggedJokesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaggedJokesFragment extends Fragment {
    private String tagName;

    public TaggedJokesFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment TaggedJokesFragment.
     */
    public static TaggedJokesFragment newInstance(String tag) {
        TaggedJokesFragment fragment = new TaggedJokesFragment();
        Bundle args = new Bundle();
        args.putString("tag", tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagName = getArguments().getString("tag");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentTaggedJokesBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_tagged_jokes, container, false);
        List<Joke> jokes = new ArrayList<>();
        JokesAdapter jokesAdapter = new JokesAdapter(getActivity(),jokes,false);
        bind.recyclerview.setAdapter(jokesAdapter);
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ViewModelProviders.of(this, new TaggedJokesViewModelFactory(tagName)).get(TaggedJokesViewModel.class)
                .getTaggedJokesLiveData().observe(this, post ->{
                    if (!jokes.contains(post)) {
                        jokes.add(post);
                        jokesAdapter.notifyDataSetChanged();
                    }

        });
        return bind.getRoot();
    }
}
