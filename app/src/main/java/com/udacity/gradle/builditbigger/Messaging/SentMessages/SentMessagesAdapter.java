package com.udacity.gradle.builditbigger.Messaging.SentMessages;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Messaging.Transcripts.TranscriptActivity;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.Models.TranscriptPreview;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.SentMessagesCellBinding;

import java.util.List;

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

    @Override @NonNull
    public SentMessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SentMessagesCellBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sent_messages_cell, parent, false);
        return new SentMessagesViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull SentMessagesViewHolder holder, int position) {
        TranscriptPreview preview = transcriptPreviews.get(position);
        holder.preview = preview;
        StringBuilder userName = new StringBuilder();
        for (HilarityUser user: preview.getConversationalists()){
            if (!user.equals(Constants.USER)){
                userName.append(user.getUserName()).append(" ");
            }
        }
        if (preview != null) {
            holder.binding.timeDateTextView.setText(Constants.formattedTimeString(context, preview.getMessage().getTimeStamp()));
            holder.binding.userNameTextView.setText(userName.toString());
            Glide.with(context).load(preview.getMessage().getHilarityUser().getUrlString()).into(holder.binding.profileImageview);
            String text = (preview.getMessage().getContents().size() == 1) ? preview.getMessage().getContents().get(0) : "Image Sent";
            holder.binding.lastMessageTextView.setText(text);
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
            Intent intent = new Intent(context, TranscriptActivity.class);
            intent.putExtra("path", preview.getPath());
            context.startActivity(intent);
        }
    }
}
