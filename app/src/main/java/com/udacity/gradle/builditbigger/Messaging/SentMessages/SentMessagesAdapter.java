package com.udacity.gradle.builditbigger.Messaging.SentMessages;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.TranscriptPreview;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.SentMessagesCellBinding;

import java.util.List;
import java.util.StringJoiner;

/**
 * Created by joeljohnson on 1/31/18.
 */

public class SentMessagesAdapter extends RecyclerView.Adapter<SentMessagesAdapter.SentMessagesViewHolder> {
    private List<TranscriptPreview> transcriptPreviews;

    public SentMessagesAdapter(List<TranscriptPreview> transcriptPreviews){
        this.transcriptPreviews = transcriptPreviews;
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
        holder.binding.lastMessageTextView.setText(preview.getTimestampString());
        String users = "";
        for(int x = 0; x < preview.getConversationalists().size(); x++){
            String name = preview.getConversationalists().get(x);
            if (!name.equals(Constants.USER.getUserName())) {
                users += name;
                if (x != preview.getConversationalists().size() - 1) users += " ";
            }
        }
        holder.binding.userNameTextView.setText(users);
        holder.binding.lastMessageTextView.setText(preview.getMessagelist().get(preview.getMessagelist().size()-1).getContents());
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
        }

        @Override
        public void onClick(View v) {
            //todo use information in preview to change fragment to the transcripts fragment
        }
    }
}
