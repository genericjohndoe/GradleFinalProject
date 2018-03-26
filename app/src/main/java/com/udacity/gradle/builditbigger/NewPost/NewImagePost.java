package com.udacity.gradle.builditbigger.NewPost;

import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Camera.LifeCycleCamera;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewImagePostBinding;

import java.io.File;

/**
 * Created by joeljohnson on 11/3/17.
 */

public class NewImagePost extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback, LoaderManager.LoaderCallbacks<Cursor> {
    //todo upon screen rotation, ensure texture view takes up entire screen
    LifeCycleCamera camera;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] mediaColumns = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DATE_TAKEN};
    private MediaAdapter mediaAdapter;
    private String number;

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
        mediaAdapter = new MediaAdapter(this, false, getActivity(), number);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //todo texttureview not rendering, fragment freezes
        FragmentNewImagePostBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_new_image_post, container, false);
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
        return bind.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            }
        }
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
        Constants.changeFragment(R.id.hilarity_content_frame, NewImageSubmission.newInstance(file, number), (AppCompatActivity) getActivity());
    }
}