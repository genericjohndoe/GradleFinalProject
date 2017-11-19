package com.udacity.gradle.builditbigger;

import android.database.Cursor;
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

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {
    //todo implement ContentResolver.openFileDescriptor to gain access to files without permissions
    private Fragment fragment;
    private Cursor cursor;

    public MediaAdapter( Fragment fragment) {
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
        cursor.moveToPosition(position);
        String path = cursor.getString(0);
        //Uri uri = new Uri.Builder().path(path).build();
//        try {
//            ParcelFileDescriptor pfd = fragment.getActivity().getContentResolver().openFileDescriptor(uri, ContentResolver.SCHEME_FILE);
//        } catch (FileNotFoundException e){
//
//        }
        Glide.with(fragment).load(new File(path))
                .into(holder.getImageView());
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
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

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }
}