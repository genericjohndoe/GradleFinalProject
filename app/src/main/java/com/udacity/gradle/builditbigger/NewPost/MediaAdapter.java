package com.udacity.gradle.builditbigger.NewPost;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.R;

import java.io.File;

/**
 * formats media to be shown in recyclerview
 */

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {
    //todo implement ContentResolver.openFileDescriptor to gain access to files without permissions
    private Fragment fragment;
    private Cursor cursor;
    private static boolean isVideo;
    private static Context context;
    private static String number;

    public MediaAdapter(Fragment fragment, boolean isVideo, Context context, String number) {
        this.fragment = fragment;
        this.isVideo = isVideo;
        this.context = context;
        this.number = number;
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
        holder.file = new File(path);
        Glide.with(fragment).load(holder.file)
                .into(holder.getImageView());
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private File file;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.gallery);
            view.setOnClickListener(this);
        }

        public ImageView getImageView() {
            return imageView;
        }

        @Override
        public void onClick(View v) {
            if (!isVideo) {
                Constants.changeFragment(R.id.hilarity_content_frame, NewImageSubmission.newInstance(file, number),(AppCompatActivity) context);
                Log.i("Hilarity", "photo");
            } else {
                Constants.changeFragment(R.id.hilarity_content_frame, NewVideoSubmission.newInstance(file, number),(AppCompatActivity) context);
                Log.i("Hilarity", "video");
            }
        }
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }
}
