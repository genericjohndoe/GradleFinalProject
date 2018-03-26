package com.udacity.gradle.builditbigger.NewPost;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.Models.MetaData;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.VideoLifeCyclerObserver;
import com.udacity.gradle.builditbigger.databinding.FragmentNewVideoSubmissionBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by joeljohnson on 11/21/17.
 */

public class NewVideoSubmission extends Fragment {
    private File file;
    private String number;
    FragmentNewVideoSubmissionBinding bind;

    public static NewVideoSubmission newInstance(File file, String number) {
        NewVideoSubmission fragment = new NewVideoSubmission();
        fragment.file = file;
        fragment.number = number;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_new_video_submission, container, false);
        Log.i("filepath", file.getParent());
        bind.submitButton.setOnClickListener(view -> {
            Constants.STORAGE.child("users/" + Constants.UID + "/videos/" + getCurrentDateAndTime() + ".mp4").putFile(Uri.fromFile(file))
                    .addOnFailureListener(exception -> {
                    })
                    .addOnSuccessListener(taskSnapshot -> {
                                String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                                DatabaseReference db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts").push();
                                Joke newVideoPost = new Joke("", "", System.currentTimeMillis(),
                                        "genre push id", downloadUrl, Constants.UID, db.getKey(), bind.videoTagline.getText().toString(), Constants.VIDEO,
                                        new MetaData("video", Integer.parseInt(number) + 1, Constants.getTags(bind.videoTagline.getText().toString())));
                                db.setValue(newVideoPost);
                            }
                    );
        });
        getLifecycle().addObserver(new VideoLifeCyclerObserver(getActivity(), bind.videopost));
        return bind.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), "Hilarity"), bandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        if (bind.videopost.getPlayer() != null) {
            bind.videopost.getPlayer().prepare(new ExtractorMediaSource(Uri.fromFile(file),
                    dataSourceFactory, extractorsFactory, null, null), false, false);
        }
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

}
