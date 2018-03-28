package com.udacity.gradle.builditbigger.NewPost.GifPost;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import com.udacity.gradle.builditbigger.Camera.LifeCycleCamera;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Interfaces.IntentCreator;
import com.udacity.gradle.builditbigger.NewPost.MediaAdapter;
import com.udacity.gradle.builditbigger.NewPost.VideoPost.VideoPostSubmissionActivity;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewGifPostBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewGifPost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewGifPost extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,  ActivityCompat.OnRequestPermissionsResultCallback, IntentCreator {

    MediaAdapter mediaAdapter;
    LifeCycleCamera camera;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private  static final int REQUEST_EXTERNAL_STORAGE_READ = 2;
    private String number;
    boolean startrecording = true;
    public NewGifPost() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NewGifPost.
     */
    public static NewGifPost newInstance(String number) {
        NewGifPost newGifPost = new NewGifPost();
        Bundle bundle = new Bundle();
        bundle.putString("number", number);
        newGifPost.setArguments(bundle);
       return new NewGifPost();
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
        if (getArguments() != null) number = getArguments().getString("number");
        mediaAdapter = new MediaAdapter(getActivity(), number, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNewGifPostBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_new_gif_post, container, false);
        camera = new LifeCycleCamera(this, bind.textureview, LifeCycleCamera.VIDEO);
        CountDownTimer cdt = new CountDownTimer(15000,1) {
            @Override
            public void onTick(long millisUntilFinished) {
                bind.timeTextView.setText("" + millisUntilFinished / 1000.0);
            }

            @Override
            public void onFinish() {
                camera.stopRecordingVideo();
                Intent intent = new Intent(getActivity(), NewGifSubmissionActivity.class);
                intent.putExtra("number", number);
                getActivity().startActivity(intent);
            }
        };

        bind.recordingImageButton.setOnClickListener(view ->{
            if (startrecording){
                camera.startRecordingVideo();
                cdt.start();
                startrecording = false;
                bind.recordingImageButton.setImageResource(R.drawable.ic_stop_black_24dp);
            } else {
                camera.stopRecordingVideo();
                cdt.cancel();
                startrecording = true;
            }
        });

        bind.switchcameraImageButton.setOnClickListener(view ->{
            camera.switchCamera();
        });

        bind.recyclerview.setAdapter(mediaAdapter);
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        return bind.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override @NonNull
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "= ?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif");
        String[] selectionArgsPdf = new String[]{ mimeType };
        return new CursorLoader(getActivity(), MediaStore.Files.getContentUri("external"), null,
                selectionMimeType, selectionArgsPdf, null);
    }

    @Override
    public void onLoadFinished(@NonNull  Loader<Cursor> loader, Cursor data) {
        data.setNotificationUri(getActivity().getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        mediaAdapter.swapCursor(data);
    }

    public void onLoaderReset(@NonNull  Loader<Cursor> loader) {
        mediaAdapter.swapCursor(null);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    public void moveFile(File file){
        Intent intent = new Intent(getActivity(), NewGifSubmissionActivity.class);
        intent.putExtra("filepath", file.getPath());
        intent.putExtra("number", number);
        getActivity().startActivity(intent);
    }

    @Override
    public void createIntent(String filepath, String number) {
        Intent intent = new Intent(getActivity(), NewGifSubmissionActivity.class);
        intent.putExtra("filepath", filepath);
        intent.putExtra("number", number);
        getActivity().startActivity(intent);
    }
}
