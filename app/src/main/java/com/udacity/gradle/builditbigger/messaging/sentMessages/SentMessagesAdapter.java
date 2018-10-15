package com.udacity.gradle.builditbigger.messaging.sentMessages;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.messaging.transcripts.TranscriptActivity;
import com.udacity.gradle.builditbigger.models.HilarityUser;
import com.udacity.gradle.builditbigger.models.TranscriptPreview;
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


    public SentMessagesAdapter(List<TranscriptPreview> transcriptPreviews, Context context) {
        this.transcriptPreviews = transcriptPreviews;
        this.context = context;
    }

    @Override
    @NonNull
    public SentMessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SentMessagesCellBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sent_messages_cell, parent, false);
        if (viewType == 0) return new SentMessagesViewHolder(bind);
        return new UnreadSentMessagesViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull SentMessagesViewHolder holder, int position) {
        TranscriptPreview preview = transcriptPreviews.get(position);
        holder.preview = preview;
        StringBuilder userName = new StringBuilder();
        for (HilarityUser user : preview.getConversationalists()) {
            if (!user.equals(Constants.USER)) {
                userName.append(user.getUserName()).append(" ");
            }
        }
        holder.binding.timeDateTextView.setText(Constants.formattedTimeString(context, preview.getMessage().getTimeStamp(), false));
        holder.binding.userNameTextView.setText(userName.toString());
        Glide.with(context).load(preview.getMessage().getHilarityUser().getUrlString()).into(holder.binding.profileImageview);
        String text = (preview.getMessage().getContents().size() == 1) ? preview.getMessage().getContents().get(0) : "Media Sent";
        holder.binding.lastMessageTextView.setText(text);

    }

    @Override
    public int getItemCount() {
        return transcriptPreviews.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (transcriptPreviews.get(position).getHasUnreadMessages()) return 1;
        return 0;
    }

    public class SentMessagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SentMessagesCellBinding binding;
        TranscriptPreview preview;

        public SentMessagesViewHolder(SentMessagesCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, TranscriptActivity.class);
            intent.putExtra(context.getString(R.string.path), preview.getPath());
            context.startActivity(intent);
        }
    }

    public class UnreadSentMessagesViewHolder extends SentMessagesViewHolder {
        public UnreadSentMessagesViewHolder(SentMessagesCellBinding binding) {
            super(binding);
            binding.getRoot().setBackgroundColor(context.getResources().getColor(R.color.green_8));
            binding.lastMessageTextView.setTextColor(context.getResources().getColor(R.color.black));
            binding.timeDateTextView.setTextColor(context.getResources().getColor(R.color.black));
            binding.userNameTextView.setTextColor(context.getResources().getColor(R.color.black));
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            v.setBackgroundColor(0);
            binding.lastMessageTextView.setTextColor(context.getResources().getColor(R.color.green_6));
            binding.timeDateTextView.setTextColor(context.getResources().getColor(R.color.green_7));
            binding.userNameTextView.setTextColor(context.getResources().getColor(R.color.green_8));
        }
    }
}
