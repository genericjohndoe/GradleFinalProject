package com.udacity.gradle.builditbigger.signInOnboarding.profilePicture;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
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
import com.udacity.gradle.builditbigger.camera.LifeCycleCamera;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.mainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentProfilePictureBinding;

import java.io.File;
import java.io.FileOutputStream;

/**
 * the class allows user to take your profile picture or select one from your device
 */
public class ProfilePictureFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE_READ = 2;
    private LifeCycleCamera camera;
    public FragmentProfilePictureBinding bind;
    private int PICK_IMAGE = 1;

    public ProfilePictureFragment() {}

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
        if (getArguments() != null) {
        }
        //check to see if permissions are granted
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
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
        });
        bind.switchCameraImageButton.setOnClickListener(view -> camera.switchCamera());
        bind.takePictureImageButton.setOnClickListener(view -> camera.takePicture());
        OvalShape ovalShape = new OvalShape();
        int size = bind.textureview.getMeasuredHeight();
        ovalShape.resize(size,size);
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        //bind.textureview.setBackground(shapeDrawable);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            if (data.getData() != null) createIntent(data.getData().getPath());
        }
    }

    /**
     * this methods submits image to storage, sets the profile url in db
     * and the Constant.USER.urlString property
     * @param filepath is path of image
     */
    public void createIntent(String filepath) {
        String path = Constants.UID + "/profilepic";
        Constants.STORAGE.child(path).putFile(Uri.fromFile(new File(filepath))).addOnSuccessListener(task -> {
            Constants.STORAGE.child(path).getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();
                Constants.USER.setUrlString(downloadUrl);
                Constants.DATABASE.child("users/" + Constants.UID + "/urlString").setValue(downloadUrl, (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        //if db is successfully updated, move on to Main UI
                        Intent intent = new Intent(getActivity(), HilarityActivity.class);
                        startActivity(intent);
                        //todo implement error handling
                    }
                });
            });

        });
    }

    /**
     * callback fired when LifeCycle Camera object captures an image
     * @param file a reference to the file object
     */
    public void moveFile(File file){
            if (camera.getMode() == LifeCycleCamera.GIF) {
                VideoToGifConverter converter = new VideoToGifConverter(getActivity(), Uri.fromFile(file));
                byte[] gif = converter.generateGIF(1);
                String path = getActivity().getCacheDir() + "/temp.gif";
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
