package com.udacity.gradle.builditbigger.Dialog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
 * Created by joeljohnson on 11/3/17.
 */

public class NewImagePost extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback, LoaderManager.LoaderCallbacks<Cursor> {
    //todo replace texture view with image if one is clicked in recyclerview
    //todo create way to go back to texture view
    //todo upon screen rotation, ensure texture view takes up entire screen
    //todo enable saving to firebase storage
    //todo change UI  to show snap button over texture view
    AutoFitTextureView textureView;
    RecyclerView recyclerView;
    LifeCycleCamera camera;
    ImageButton snap;
    ImageButton switchCamera;
    private String GALLERY_LOCATION = "hilarity_image";
    //private File mGalleryFolder;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] mediaColumns = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DATE_TAKEN};
    private MediaAdapter mediaAdapter;
    boolean justSanpped;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStorageWritePermission();
            return;
        }
        mediaAdapter = new MediaAdapter(this);
        justSanpped = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_image_post_preview, container, false);
        textureView = root.findViewById(R.id.textureView);
        snap = root.findViewById(R.id.takepicture_imageButton);
        switchCamera = root.findViewById(R.id.switchcamera_imageButton);
        recyclerView = root.findViewById(R.id.photo_thumbnail_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(mediaAdapter);
        camera = new LifeCycleCamera(this, textureView, LifeCycleCamera.PHOTO);
        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture();
                justSanpped = true;
            }
        });
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.switchCamera();
            }
        });
        return root;
    }

//    private void createImageGallery() {
//        //todo ensure all folders in dcim are shown
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestStorageWritePermission();
//            return;
//        }
//        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        mGalleryFolder = new File(storageDirectory.toString());
//        if (!mGalleryFolder.exists()) {
//            mGalleryFolder.mkdirs();
//        }
//    }

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
        if (justSanpped) {
            Constants.STORAGE.child("users/" + Constants.UID + "/images/" + getCurrentDateAndTime() + ".png").putFile(camera.getFilePath())
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

    public void onLoaderReset(Loader<Cursor> loader) {
        mediaAdapter.swapCursor(null);
    }

    public void continueToSubmit(String filename) {
        Bundle bundle = new Bundle();
        bundle.putString("filepath", filename);
        Log.i("file name 1", filename);
        NewImageSubmission nis = new NewImageSubmission();
        nis.setArguments(bundle);
        getParentFragment().getChildFragmentManager()
                .beginTransaction().replace(R.id.new_post_fragment, nis)
                .addToBackStack(null).commit();
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
