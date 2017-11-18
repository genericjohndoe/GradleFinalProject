package com.udacity.gradle.builditbigger;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by joeljohnson on 11/15/17.
 */

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback {
    private File imagesFile;
    private Fragment fragment;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    public MediaAdapter(File folderFile, Fragment fragment) {
        imagesFile = folderFile;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_imageview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File imageFile = imagesFile.listFiles()[position];
        Glide.with(fragment).load(imageFile)
                .into(holder.getImageView());
    }

    @Override
    public int getItemCount() {
        return imagesFile.listFiles(new PhotoFilter()).length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.gallery);
        }

        public ImageView getImageView() {
            return imageView;
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
        if (fragment.shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            fragment.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        }
    }
}
