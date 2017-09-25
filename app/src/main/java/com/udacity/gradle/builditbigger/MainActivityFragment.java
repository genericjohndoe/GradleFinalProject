package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements RecyclerViewCallback {

    RecyclerView recyclerview;
    ImageButton imageButton;
    LanguageAdapter languageAdapter;
    List<String> languageList;

    public MainActivityFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languageAdapter = new LanguageAdapter(getActivity(), this);
        languageList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerview = (RecyclerView) root.findViewById(R.id.languages_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(languageAdapter);
        imageButton = (ImageButton) root.findViewById(R.id.proceed);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GenreActivity.class);
                intent.putExtra(getActivity().getString(R.string.languages), languageList.get(0) + " Genres");
                getActivity().startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void passItem(String item) {
        languageList.add(item);
        Log.i("joke", ""+languageList.size());
    }
}

