package com.udacity.gradle.builditbigger.NewPost.AudioMediaPost;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.NewPost.MediaAdapter;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentAudioMediaPostBinding;

import android.Manifest;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudioMediaPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioMediaPostFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private String number;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String[] mediaColumns = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DATE_TAKEN};
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private MediaRecorder recorder;
    private String fileName;
    private boolean isRecording = true;
    private long stopTime = 0;
    private MediaAdapter mediaAdapter;

    public AudioMediaPostFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AudioMediaPostFragment.
     */
    public static AudioMediaPostFragment newInstance(String number) {
        AudioMediaPostFragment fragment = new AudioMediaPostFragment();
        Bundle args = new Bundle();
        args.putString("number", number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) number = getArguments().getString("number");
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        fileName = getActivity().getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";
        mediaAdapter = new MediaAdapter(getActivity(), number, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAudioMediaPostBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_audio_media_post, container, false);

        bind.recordButton.setOnClickListener(view -> {
            if (isRecording){
                if (android.os.Build.VERSION.SDK_INT >= 24) bind.pauseButton.setVisibility(View.VISIBLE);
                bind.stopButton.setVisibility(View.VISIBLE);
                bind.recordButton.setVisibility(View.GONE);
                startRecording();
            } else {
                if (android.os.Build.VERSION.SDK_INT >= 24) recorder.resume();
                bind.recordButton.setVisibility(View.GONE);
                bind.pauseButton.setVisibility(View.VISIBLE);
                isRecording = true;
            }
            bind.timerTextView.setBase(SystemClock.elapsedRealtime() + stopTime);
            bind.timerTextView.start();
        });
        bind.pauseButton.setOnClickListener(view -> {
            if (android.os.Build.VERSION.SDK_INT >= 24) {
                recorder.pause();
                bind.recordButton.setVisibility(View.VISIBLE);
                bind.pauseButton.setVisibility(View.GONE);
                isRecording = false;
                stopTime = bind.timerTextView.getBase() - SystemClock.elapsedRealtime();
                bind.timerTextView.stop();
            }
        });
        bind.stopButton.setOnClickListener(view -> {
            stopRecording();
            bind.recordButton.setVisibility(View.VISIBLE);
            bind.stopButton.setVisibility(View.GONE);
            bind.pauseButton.setVisibility(View.GONE);
            isRecording = true;
            bind.timerTextView.setBase(SystemClock.elapsedRealtime());
            stopTime = 0;
            bind.timerTextView.stop();
            bind.timerTextView.setText("00:00");
            Intent intent = new Intent(getActivity(), AudioMediaPostSubmissionActivity.class);
            intent.putExtra("number", number);
            intent.putExtra("path", fileName);
            startActivity(intent);
        });
        bind.audioRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        bind.audioRecyclerView.setAdapter(mediaAdapter);
        return bind.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) getActivity().finish();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null,  this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override @NonNull
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
        return new CursorLoader(getActivity(), MediaStore.Files.getContentUri("external"), mediaColumns,
                selection, null, mediaColumns[1] + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        data.setNotificationUri(getActivity().getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mediaAdapter.swapCursor(data, true);
    }

    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mediaAdapter.swapCursor(null, true);
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("hilarity_recorder", "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}
