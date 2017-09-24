package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by joeljohnson on 7/25/17.
 */

public class JokeFragment extends Fragment {
    String body;
    String title;
    String author;
    TextView jokeBody;
    TextView jokeTitle;
    TextView userName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();

        body = bundle.getString(getString(R.string.jokeBody));
        title = bundle.getString(getString(R.string.jokeTitle));
        author = bundle.getString(getString(R.string.userName));
        Log.i("joke",title + " " + author);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_joke, container, false);

        jokeBody = (TextView) rootView.findViewById(R.id.joke_body);
        jokeTitle = (TextView) rootView.findViewById(R.id.joke_title);
        userName = (TextView) rootView.findViewById(R.id.joke_author);

        jokeBody.setText(body);
        jokeTitle.setText(title);
        userName.setText(author);

        return rootView;
    }
}
