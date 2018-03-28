package com.udacity.gradle.builditbigger.NewPost;

import android.content.Context;
import android.content.Intent;
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
import com.udacity.gradle.builditbigger.NewPost.ImagePost.ImagePostSubmissionActivity;
import com.udacity.gradle.builditbigger.NewPost.ImagePost.NewImageSubmission;
import com.udacity.gradle.builditbigger.NewPost.VideoPost.NewVideoSubmission;
import com.udacity.gradle.builditbigger.NewPost.VideoPost.VideoPostSubmissionActivity;
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
        holder.filepath = path;
        Glide.with(fragment).load(new File(path))
                .into(holder.getImageView());
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private String filepath;

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
                createIntent(ImagePostSubmissionActivity.class, filepath,number);
            } else {
                createIntent(VideoPostSubmissionActivity.class,filepath, number);
            }
        }

        private void createIntent(Class activity, String filepath, String number){
            Intent intent = new Intent(context, activity);
            intent.putExtra("filepath", filepath);
            intent.putExtra("number", number);
            context.startActivity(intent);
        }
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

}
