package com.udacity.gradle.builditbigger.NewPost.VideoPost;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Camera.LifeCycleCamera;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Interfaces.IntentCreator;
import com.udacity.gradle.builditbigger.Interfaces.ReturnMediaResult;
import com.udacity.gradle.builditbigger.NewPost.MediaAdapter;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewVideoPostBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by joeljohnson on 11/4/17.
 */

public class NewVideoPost extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ActivityCompat.OnRequestPermissionsResultCallback, IntentCreator, ReturnMediaResult {
    //todo take into account screen rotation such that textureview takes up entire screen in landscape mode
    //todo error handling for screen rotation
    LifeCycleCamera camera;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] mediaColumns = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns.DATE_TAKEN};
    private MediaAdapter mediaAdapter;
    Handler handler;
    boolean startrecording = true;
    int Seconds, Minutes, MilliSeconds;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    private String number;
    FragmentNewVideoPostBinding bind;

    public static NewVideoPost newInstance(String number) {
        NewVideoPost newVideoPost = new NewVideoPost();
        newVideoPost.number = number;
        return newVideoPost;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStorageWritePermission();
            return;
        }
        mediaAdapter = new MediaAdapter(getActivity(), number, this, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater,R.layout.fragment_new_video_post, container, false);
        handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                MillisecondTime = SystemClock.uptimeMillis() - StartTime;
                UpdateTime = TimeBuff + MillisecondTime;
                Seconds = (int) (UpdateTime / 1000);
                Minutes = Seconds / 60;
                Seconds = Seconds % 60;
                MilliSeconds = (int) (UpdateTime % 1000);
                bind.timer.setText("" + Minutes + ":"
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds));
                handler.postDelayed(this, 0);
            }
        };
        bind.videoThumbnailRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        bind.videoThumbnailRecyclerview.setAdapter(mediaAdapter);
        camera = new LifeCycleCamera(this, bind.textureView, LifeCycleCamera.VIDEO);
        bind.recordingImageButton.setOnClickListener(view ->{
                if (startrecording) {
                    camera.startRecordingVideo();
                    startrecording = !startrecording;
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    bind.recordingImageButton.setImageResource(R.drawable.ic_stop_black_24dp);
                } else {
                    startrecording = !startrecording;
                    handler.removeCallbacks(runnable);
                    bind.timer.setText("0:00:00:000");
                    camera.stopRecordingVideo();
                }
        });
        bind.switchcameraImageButton.setOnClickListener(view -> {camera.switchCamera();});
        bind.textureView.setVisibility(View.GONE);
        return bind.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        bind.textureView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        bind.textureView.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void requestStorageWritePermission() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns,
                null, null, mediaColumns[1] + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.setNotificationUri(getActivity().getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        mediaAdapter.swapCursor(data, false);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mediaAdapter.swapCursor(null, false);
    }

    public void moveFile(File file){
        if (!number.equals("-1")) {
            Intent intent = new Intent(getActivity(), VideoPostSubmissionActivity.class);
            intent.putExtra("filepath", file.getPath());
            intent.putExtra("number", number);
            getActivity().startActivity(intent);
        } else {
            returnResult(file.getPath());
        }
    }

    @Override
    public void createIntent(String filepath, String number) {
        Intent intent = new Intent(getActivity(), VideoPostSubmissionActivity.class);
        intent.putExtra("filepath", filepath);
        intent.putExtra("number", number);
        getActivity().startActivity(intent);
    }

    @Override
    public void returnResult(String path) {
        Intent intent = new Intent();
        intent.putExtra("filepath",path);
        getActivity().setResult(1, intent);
        getActivity().finish();
    }
}
