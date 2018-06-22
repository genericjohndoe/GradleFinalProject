package com.udacity.gradle.builditbigger.NewPost.ImagePost;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Camera.LifeCycleCamera;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Interfaces.IntentCreator;
import com.udacity.gradle.builditbigger.Interfaces.ReturnMediaResult;
import com.udacity.gradle.builditbigger.NewPost.MediaAdapter;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewImagePostBinding;

import java.io.File;

/**
 * Created by joeljohnson on 11/3/17.
 */

public class NewImagePost extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback, LoaderManager.LoaderCallbacks<Cursor>, IntentCreator, ReturnMediaResult {
    //todo upon screen rotation, ensure texture view takes up entire screen
    LifeCycleCamera camera;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] mediaColumns = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DATE_TAKEN};
    private MediaAdapter mediaAdapter;
    private String number;
    FragmentNewImagePostBinding bind;

    public static NewImagePost newInstance(String number) {
        NewImagePost newImagePost = new NewImagePost();
        newImagePost.number = number;
        return newImagePost;
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
        bind = DataBindingUtil.inflate(inflater,R.layout.fragment_new_image_post, container, false);
        bind.photoThumbnailRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        bind.photoThumbnailRecyclerview.setAdapter(mediaAdapter);
        camera = new LifeCycleCamera(this, bind.textureView, LifeCycleCamera.PHOTO);
        bind.takepictureImageButton.setOnClickListener(view -> {
                    camera.takePicture();
                }
        );
        bind.switchcameraImageButton.setOnClickListener(view -> {
                    camera.switchCamera();
                }
        );
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {}
        }
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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediaColumns,
                null, null, mediaColumns[1] + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.setNotificationUri(getActivity().getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mediaAdapter.swapCursor(data);

    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mediaAdapter.swapCursor(null);
    }

    public void moveFile(File file){
        if (!number.equals("-1")) {
            Intent intent = new Intent(getActivity(), ImagePostSubmissionActivity.class);
            intent.putExtra("filepath", file.getPath());
            intent.putExtra("number", number);
            getActivity().startActivity(intent);
            Log.i("newmediatest", "number isn't -1");
        }else {
            Log.i("newmediatest", "number is -1");
            returnResult(file.getPath());
        }
    }

    @Override
    public void createIntent(String filepath, String number) {
        Intent intent = new Intent(getActivity(), ImagePostSubmissionActivity.class);
        intent.putExtra("filepath", filepath);
        intent.putExtra("number", number);
        getActivity().startActivity(intent);
    }

    @Override
    public void returnResult(String path) {
        Log.i("newmediatest", "activity should finish and return result");
        Intent intent = new Intent();
        intent.putExtra("filepath",path);
        getActivity().setResult(1, intent);
        getActivity().finish();
    }
}
