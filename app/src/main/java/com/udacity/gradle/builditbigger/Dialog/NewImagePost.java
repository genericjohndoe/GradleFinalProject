package com.udacity.gradle.builditbigger.Dialog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.udacity.gradle.builditbigger.Camera.AutoFitTextureView;
import com.udacity.gradle.builditbigger.Camera.LifeCycleCamera;
import com.udacity.gradle.builditbigger.MediaAdapter;
import com.udacity.gradle.builditbigger.R;

import java.io.File;

/**
 * Created by joeljohnson on 11/3/17.
 */

public class NewImagePost extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    //TODO change UI to open camera and show horizontal linear recyclerview below
    //todo ensure camera is still to camera mode
    //todo ensure camera object is deleted when fragment dies
    AutoFitTextureView textureView;
    RecyclerView recyclerView;
    LifeCycleCamera camera;
    Button snap;
    private String GALLERY_LOCATION = "hilarity_image";
    private File mGalleryFolder;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createImageGallery();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_image_post, container, false);
        textureView = root.findViewById(R.id.textureView);
        snap = root.findViewById(R.id.snap);
        recyclerView = root.findViewById(R.id.photo_thumbnail_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new MediaAdapter(mGalleryFolder, this));
        camera = new LifeCycleCamera(this, textureView, LifeCycleCamera.PHOTO);
        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture();
            }
        });
        return root;
    }

    private void createImageGallery() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStorageWritePermission();
            return;
        }
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        mGalleryFolder = new File(storageDirectory.toString()+"/Camera");
        if (!mGalleryFolder.exists()) {
            mGalleryFolder.mkdirs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
               /* ErrorDialog.newInstance(getString(R.string.request_permission))
                        .show(getChildFragmentManager(), FRAGMENT_DIALOG);*/
            }
        }
    }

    private void requestStorageWritePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        }
    }
}
