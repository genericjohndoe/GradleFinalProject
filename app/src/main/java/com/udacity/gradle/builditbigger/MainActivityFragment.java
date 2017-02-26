package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gjd.mylibrary.MyLibraryActivity;
import com.udacity.gradle.jokes.Joker;

import static java.security.AccessController.getContext;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    EndpointsAsyncTask task;
    static String asyncTaskOutputString;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        Button button = (Button) root.findViewById(R.id.joke_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Joker joker = new Joker();
                Intent myIntent = new Intent(getContext(), MyLibraryActivity.class);
                myIntent.putExtra(getString(R.string.joke_key), joker.getJoke());
                startActivity(myIntent);
            }
        });
        /*Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                .setRootUrl("http://<my-computer-address>:8080/_ah/api/");*/
        task = new EndpointsAsyncTask();
        task.execute(new Pair(getContext(), "Manfred"));
        if (task.getStatus().toString().equals(AsyncTask.Status.FINISHED)){
            ((AsyncOutput) getActivity()).asyncTaskOutput(asyncTaskOutputString);
        }

        return root;
    }


    public void startTask(){
        task = new EndpointsAsyncTask();
        task.execute(new Pair(getContext(), "Manfred"));
    }

    public interface AsyncOutput {
        public void asyncTaskOutput(String finalString);
    }
}
