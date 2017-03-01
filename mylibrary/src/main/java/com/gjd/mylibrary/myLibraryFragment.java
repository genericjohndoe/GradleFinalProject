package com.gjd.mylibrary;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by joeljohnson on 1/25/17.
 */

public class myLibraryFragment extends Fragment {

    public myLibraryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image, container, false);
        TextView textview = (TextView) root. findViewById(R.id.libraryWelcomeTextView);
        textview.setText(getActivity().getIntent().getExtras().getString(getString(R.string.joke_key)));
        return root;
    }
}
