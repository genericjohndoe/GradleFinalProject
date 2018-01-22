package com.udacity.gradle.builditbigger;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.udacity.gradle.builditbigger.Interfaces.RecyclerViewCallback;
import com.udacity.gradle.builditbigger.Language.LanguageAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements RecyclerViewCallback {

    RecyclerView recyclerview;
    ImageButton imageButton;
    LanguageAdapter languageAdapter;
    List<String> languageList;


    public MainActivityFragment() {
    }

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
        recyclerview = root.findViewById(R.id.languages_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(languageAdapter);
//        imageButton = root.findViewById(R.id.proceed);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), GenreActivity.class);
//                getActivity().startActivity(intent);
//            }
//        });
        return root;
    }

    @Override
    public void passItem(String item) {
        languageList.add(item);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(getString(R.string.preference_saved_languages_set), new HashSet<String>(languageList));
        if (editor.commit()) {
            Toast toast = Toast.makeText(getActivity(), "language added", Toast.LENGTH_SHORT);
            toast.show();
        }
        Log.i("joke", "" + languageList.size());
    }
}

