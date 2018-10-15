package com.udacity.gradle.builditbigger.newPost;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.interfaces.IntentCreator;
import com.udacity.gradle.builditbigger.interfaces.ReturnMediaResult;
import com.udacity.gradle.builditbigger.R;

import java.io.File;

/**
 * formats media to be shown in recyclerview
 */

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {
    //todo implement ContentResolver.openFileDescriptor to gain access to files without permissions
    private Cursor cursor;
    private Context context;
    private static String number;
    private static IntentCreator creator;
    private static ReturnMediaResult returnMediaResult;
    private boolean isAudio;

    public MediaAdapter(Context context, String number, IntentCreator creator, ReturnMediaResult returnMediaResult) {
        this.context = context;
        this.number = number;
        this.creator = creator;
        this.returnMediaResult = returnMediaResult;
    }

    public MediaAdapter(Context context, String number, IntentCreator creator) {
        this.context = context;
        this.number = number;
        this.creator = creator;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_imageview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        String path = cursor.getString(0);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        holder.filepath = path;
        try {
            mmr.setDataSource(context, Uri.fromFile(new File(path)));
        } catch (Exception e){
            Log.i("mediaadapter", e.toString());
        }
        if (!isAudio) {
            Glide.with(context).load(getVisualMediaTypeDrawable(path,mmr))
                    .into(holder.type);
            holder.type.setContentDescription(getVisualMediaContentDescription(path,mmr));
        }

        Glide.with(context).load(isAudio ? mmr.getEmbeddedPicture() : new File(path))
                    .into(holder.imageView);

        mmr.release();
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private ImageView type;
        private String filepath;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.gallery);
            type = view.findViewById(R.id.type);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (!number.equals("-1")) {
                creator.createIntent(filepath, number);
            } else {
                returnMediaResult.returnResult(filepath);
            }
        }
    }

    public void swapCursor(Cursor newCursor, boolean isAudio) {
        cursor = newCursor;
        this.isAudio = isAudio;
        notifyDataSetChanged();
    }

    private int getVisualMediaTypeDrawable(String path, MediaMetadataRetriever mmr){
        if (path.contains(".gif")) return R.drawable.ic_gif_black_24dp;
        String isVideo = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
        if (isVideo == null) {
            return R.drawable.ic_menu_camera;
        } else {
            return R.drawable.ic_videocam_white_24dp;
        }
    }

    private String getVisualMediaContentDescription(String path, MediaMetadataRetriever mmr){
        if (path.contains(".gif")) return context.getString(R.string.gif);
        String isVideo = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
        if (isVideo == null) {
            return context.getString(R.string.photo);
        } else {
            return context.getString(R.string.video);
        }
    }
}
