package com.udacity.gradle.builditbigger.Messaging.SentMessages;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Messaging.Transcripts.TranscriptFragment;
import com.udacity.gradle.builditbigger.Models.TranscriptPreview;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.SentMessagesCellBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * SentMessagesAdapter class styles and provides message data for recyckerview
 */

public class SentMessagesAdapter extends RecyclerView.Adapter<SentMessagesAdapter.SentMessagesViewHolder> {
    //todo find way to show multiple profile images
    private List<TranscriptPreview> transcriptPreviews;
    private Context context;


    public SentMessagesAdapter(List<TranscriptPreview> transcriptPreviews, Context context){
        this.transcriptPreviews = transcriptPreviews;
        this.context = context;
    }

    @Override
    public SentMessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SentMessagesCellBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sent_messages_cell, parent, false);
        return new SentMessagesViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(SentMessagesViewHolder holder, int position) {
        TranscriptPreview preview = transcriptPreviews.get(position);
        holder.preview = preview;
        if (preview != null) {
            holder.binding.timeDateTextView.setText(Constants.formattedTimeString(context, preview.getMessage().getTimeStamp()));
            holder.binding.userNameTextView.setText(preview.getMessage().getHilarityUser().getUserName());
            Glide.with(context).load(preview.getMessage().getHilarityUser().getUrlString()).into(holder.binding.profileImageview);
            holder.binding.lastMessageTextView.setText(preview.getMessage().getContents());
        } else {
            Log.i("Hilarity","preview is null");
        }
    }

    @Override
    public int getItemCount() {
        return transcriptPreviews.size();
    }

    public class SentMessagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        SentMessagesCellBinding binding;
        TranscriptPreview preview;

        public SentMessagesViewHolder(SentMessagesCellBinding binding){
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Constants.changeFragment(R.id.hilarity_content_frame, TranscriptFragment.newInstance(Constants.UID, preview.getPath()), (AppCompatActivity) context);
            Log.i("Hilarity", "SentMessagesViewHolder clicked ");
        }

    }
}
