package com.udacity.gradle.builditbigger.Language;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Interfaces.RecyclerViewCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import agency.tango.materialintroscreen.SlideFragment;

/**
 * Created by joeljohnson on 10/3/17.
 */

public class LanguageSelectorFragment extends SlideFragment implements RecyclerViewCallback {

    RecyclerView recyclerview;
    LanguageAdapter languageAdapter;
    List<String> languageList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languageAdapter = new LanguageAdapter(getActivity(), this);
        languageList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerview = view.findViewById(R.id.languages_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(languageAdapter);
        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.primary;
    }

    @Override
    public int buttonsColor() {
        return R.color.accent;
    }

    @Override
    public boolean canMoveFurther() {
        return languageList.size() >= 1;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "No Language Selected";
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
