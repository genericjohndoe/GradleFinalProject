package com.udacity.gradle.builditbigger.NewPost.AudioMediaPost;


import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentAudioMediaPostSubmissionBinding;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudioMediaPostSubmissionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioMediaPostSubmissionFragment extends Fragment {
    private String number;
    private String audioFilePath;

    public AudioMediaPostSubmissionFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AudioMediaPostSubmissionFragment.
     */
    public static AudioMediaPostSubmissionFragment newInstance(String number, String audioFilePath) {
        AudioMediaPostSubmissionFragment fragment = new AudioMediaPostSubmissionFragment();
        Bundle args = new Bundle();
        args.putString("number", number);
        args.putString("path", audioFilePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            number = getArguments().getString("number");
            audioFilePath = getArguments().getString("path");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAudioMediaPostSubmissionBinding bind = DataBindingUtil
                .inflate(inflater, R.layout.fragment_audio_media_post_submission, container, false);
        bind.playButton.setOnClickListener(view -> {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioFilePath);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {}
        });
        return bind.getRoot();
    }

}
