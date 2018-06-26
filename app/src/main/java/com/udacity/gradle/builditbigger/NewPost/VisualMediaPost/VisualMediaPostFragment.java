package com.udacity.gradle.builditbigger.NewPost.VisualMediaPost;

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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import com.udacity.gradle.builditbigger.Camera.LifeCycleCamera;
import com.udacity.gradle.builditbigger.Interfaces.IntentCreator;
import com.udacity.gradle.builditbigger.NewPost.MediaAdapter;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentVisualMediaPostBinding;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VisualMediaPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisualMediaPostFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback, LoaderManager.LoaderCallbacks<Cursor>, IntentCreator {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE_READ = 2;
    private String[] mediaColumns = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DATE_TAKEN};
    private MediaAdapter mediaAdapter;
    private LifeCycleCamera camera;
    Handler handler;
    boolean startrecording = true;
    int Seconds, Minutes, MilliSeconds;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    private String number;
    public VisualMediaPostFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment VisualMediaPostFragment.
     */
    public static VisualMediaPostFragment newInstance(String number) {
        VisualMediaPostFragment fragment = new VisualMediaPostFragment();
        Bundle bundle = new Bundle();
        bundle.putString("number", number);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStorageWritePermission();
            return;
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStorageReadPermission();
            return;
        }
        if (getArguments() != null){
            number = getArguments().getString("number");
        }
        mediaAdapter = new MediaAdapter(getActivity(), number, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentVisualMediaPostBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_visual_media_post, container, false);
        bind.recyclerView.setAdapter(mediaAdapter);
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        camera = new LifeCycleCamera(this, bind.autoFitTextureView, LifeCycleCamera.PHOTO);
        handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                MillisecondTime = SystemClock.uptimeMillis() - StartTime;
                UpdateTime = TimeBuff + MillisecondTime;
                Seconds = (int) (UpdateTime / 1000);
                Minutes = Seconds / 60;
                Seconds = Seconds % 60;
                MilliSeconds = (int) (UpdateTime % 1000);
                bind.timerTextView.setText("" + Minutes + ":"
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds));
                handler.postDelayed(this, 0);
            }
        };
        bind.photoButton.setOnClickListener(view -> {
            if (camera.getMode() != LifeCycleCamera.PHOTO){
                Log.i("buttonz", "photo button clicked");
                bind.gifButton.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                bind.videoButton.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                bind.photoButton.setBackgroundColor(getResources().getColor(R.color.green_9));
                bind.timerTextView.setVisibility(View.GONE);
                camera.setMode(LifeCycleCamera.PHOTO);
            }
        });
        bind.videoButton.setOnClickListener(view -> {
            if (camera.getMode() != LifeCycleCamera.VIDEO){
                Log.i("buttonz", "video button clicked");
                bind.gifButton.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                bind.videoButton.setBackgroundColor(getResources().getColor(R.color.green_9));
                bind.photoButton.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                bind.timerTextView.setVisibility(View.VISIBLE);
                camera.setMode(LifeCycleCamera.VIDEO);
            }
        });
        bind.gifButton.setOnClickListener(view -> {
            if (camera.getMode() != LifeCycleCamera.GIF){
                Log.i("buttonz", "gif button clicked");
                bind.gifButton.setBackgroundColor(getResources().getColor(R.color.green_9));
                bind.videoButton.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                bind.photoButton.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                bind.timerTextView.setVisibility(View.VISIBLE);
                camera.setMode(LifeCycleCamera.GIF);
            }
        });
        bind.switchCameraButton.setOnClickListener(view -> camera.switchCamera());
        bind.captureButton.setOnClickListener(view -> {
            if (camera.getMode() == LifeCycleCamera.PHOTO){
                camera.takePicture();
            } else if (camera.getMode() == LifeCycleCamera.VIDEO){
                if (startrecording) {
                    camera.startRecordingVideo();
                    startrecording = !startrecording;
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    bind.captureButton.setImageResource(R.drawable.ic_stop_black_24dp);
                } else {
                    startrecording = !startrecording;
                    handler.removeCallbacks(runnable);
                    bind.timerTextView.setText("0:00:00.000");
                    camera.stopRecordingVideo();
                }
            } else {
                //todo add in code for gifs
            }
        });

        return bind.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {}
        }
    }

    private void requestStorageWritePermission() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void requestStorageReadPermission() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_READ);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null,  this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override @NonNull
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
                //+ " OR "
                //+ MediaStore.Files.FileColumns.MIME_TYPE + "= ?";
        //String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif");
        //String[] selectionArgs = new String[]{ mimeType };
        return new CursorLoader(getActivity(), MediaStore.Files.getContentUri("external"), mediaColumns,
                selection, null, mediaColumns[1] + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        data.setNotificationUri(getActivity().getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mediaAdapter.swapCursor(data, false);
    }

    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mediaAdapter.swapCursor(null, false);
    }

    @Override
    public void createIntent(String filepath, String number) {
        Intent intent = new Intent(getActivity(), VisualMediaPostSubmissionActivity.class);
        intent.putExtra("filepath", filepath);
        intent.putExtra("number", number);
        startActivity(intent);
    }

    public void moveFile(File file){
        createIntent(file.getPath(),number);
    }
}
