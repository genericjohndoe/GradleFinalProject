package com.udacity.gradle.builditbigger.Dialog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.udacity.gradle.builditbigger.Camera.AutoFitTextureView;
import com.udacity.gradle.builditbigger.Camera.LifeCycleCamera;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by joeljohnson on 11/4/17.
 */

public class NewVideoPost extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ActivityCompat.OnRequestPermissionsResultCallback {
    //TODO modify UI to show how long user has been recording, switch between textureview and videoview
    //todo take into account screen rotation such that textureview takes up entire screen in landscape mode
    //todo after movie is selected, populate dialog with another fragment allow the user to add a tag line and submit
    //todo ensure camera object is deleted when fragment dies
    //todo error handling for screen rotation
    AutoFitTextureView textureView;
    RecyclerView recyclerView;
    LifeCycleCamera camera;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] mediaColumns = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns.DATE_TAKEN};
    private MediaAdapter mediaAdapter;
   // Button record;
    TextView timer;
    ImageButton recordImageButton;
    ImageButton switchCamera;
    Handler handler;
    boolean startrecording = true;
    int Seconds, Minutes, MilliSeconds;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStorageWritePermission();
            return;
        }
        mediaAdapter = new MediaAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_video_post_preview, container, false);
        handler = new Handler();
        final Runnable runnable = new Runnable() {

            public void run() {

                MillisecondTime = SystemClock.uptimeMillis() - StartTime;

                UpdateTime = TimeBuff + MillisecondTime;

                Seconds = (int) (UpdateTime / 1000);

                Minutes = Seconds / 60;

                Seconds = Seconds % 60;

                MilliSeconds = (int) (UpdateTime % 1000);

                timer.setText("" + Minutes + ":"
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds));

                handler.postDelayed(this, 0);
            }

        };
        timer = root.findViewById(R.id.timer);
        textureView = root.findViewById(R.id.textureView);
        recyclerView = root.findViewById(R.id.video_thumbnail_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(mediaAdapter);
        camera = new LifeCycleCamera(this, textureView, LifeCycleCamera.VIDEO);
        recordImageButton = root.findViewById(R.id.recording_imageButton);
        recordImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startrecording) {
                    camera.startRecordingVideo();
                    startrecording = !startrecording;
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    recordImageButton.setImageResource(R.drawable.ic_stop_black_24dp);
                } else {
                    camera.stopRecordingVideo();
                    startrecording = !startrecording;
                    handler.removeCallbacks(runnable);
                    timer.setText("0:00:00:000");
                    Constants.STORAGE.child("users/" + Constants.UID + "/videos/" + getCurrentDateAndTime() + ".mp4").putFile(camera.getFilePath())
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //todo tell user upload failed
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                                    continueToSubmit(downloadUrl);
                                }
                            });
                }
            }
        });
        switchCamera = root.findViewById(R.id.switchcamera_imageButton);
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.switchCamera();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void requestStorageWritePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
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
        mediaAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mediaAdapter.swapCursor(null);
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public void continueToSubmit(String filename) {
        Bundle bundle = new Bundle();
        bundle.putString("filepath", filename);
        Log.i("file name 1", filename);
        NewVideoSubmission nvs = new NewVideoSubmission();
        nvs.setArguments(bundle);
        getParentFragment().getChildFragmentManager()
                .beginTransaction().replace(R.id.new_post_fragment, nvs)
                .addToBackStack(null).commit();
    }
}
