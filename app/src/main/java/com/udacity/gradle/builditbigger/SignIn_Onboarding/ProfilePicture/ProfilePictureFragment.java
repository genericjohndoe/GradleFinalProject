package com.udacity.gradle.builditbigger.SignIn_Onboarding.ProfilePicture;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esp.videotogifconverter.VideoToGifConverter;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.udacity.gradle.builditbigger.Camera.AutoFitTextureView;
import com.udacity.gradle.builditbigger.Camera.LifeCycleCamera;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentProfilePictureBinding;

import java.io.File;
import java.io.FileOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePictureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePictureFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE_READ = 2;
    private LifeCycleCamera camera;
    private FirebaseVisionImage image;
    private AutoFitTextureView autoFitTextureView;
    private FirebaseVisionFaceDetectorOptions options;
    private FirebaseVisionFaceDetector detector;
    public FragmentProfilePictureBinding bind;
    private int PICK_IMAGE = 1;

    public ProfilePictureFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfilePictureFragment.
     */
    public static ProfilePictureFragment newInstance() {
        ProfilePictureFragment fragment = new ProfilePictureFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStorageWritePermission();
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStorageReadPermission();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_picture, container, false);
        camera = new LifeCycleCamera(this, bind.textureview, LifeCycleCamera.PHOTO, false, true);
        bind.galleryImageButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });
        bind.switchCameraImageButton.setOnClickListener(view -> camera.switchCamera());
        bind.takePictureImageButton.setOnClickListener(view -> camera.takePicture());
        return bind.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            if (uri != null)
                createIntent(uri.getPath());
        }
    }

    public void createIntent(String filepath) {
        //save to storage, add storage path (user settings) to rtd
        //when down url (user data path) is generated, save then start intent for new activity
        Intent intent = null;
        startActivity(intent);
    }

    public void moveFile(File file){
        if (camera.getMode() == LifeCycleCamera.GIF){
            VideoToGifConverter converter = new VideoToGifConverter(getActivity(), Uri.fromFile(file));
            byte[] gif = converter.generateGIF(1);
            String path = getActivity().getCacheDir()+"/temp.gif";
            FileOutputStream stream = null;
            try {
                stream = new FileOutputStream(path);
                stream.write(gif);
                stream.close();
            } catch (Exception e) {

            }
            createIntent(path);
        } else {
            createIntent(file.getPath());
        }
    }
}
