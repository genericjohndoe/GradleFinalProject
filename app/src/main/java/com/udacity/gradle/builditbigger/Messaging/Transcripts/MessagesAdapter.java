package com.udacity.gradle.builditbigger.Messaging.Transcripts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Message;
import com.udacity.gradle.builditbigger.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * MessagesAdapter class used to format Message objects for UI
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.SentMessagesViewHolder> {
    private List<Message> messages;
    private Context context;
    private static final int TYPE_SENT = 0;
    private static final int TYPE_RECEIVED = 1;

    public MessagesAdapter(List<Message> messages, Context context){
        this.messages = messages;
        this.context = context;
    }

    @Override
    public SentMessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch  (viewType){
            case TYPE_SENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message_layout, parent, false);
                return new SentMessagesViewHolder(view);
            case TYPE_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recieved_message_layout, parent, false);
                return new RecievedMessagesViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(SentMessagesViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.content.setText(message.getContents());
        holder.timeSent.setText(Constants.formattedTimeString(context,message.getTimeStamp()));
        if (holder instanceof RecievedMessagesViewHolder){
            ((RecievedMessagesViewHolder) holder).user.setText(message.getHilarityUser().getUserName());
            Glide.with(context).load(message.getHilarityUser().getUrlString())
                    .into(((RecievedMessagesViewHolder) holder).profileImg);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getHilarityUser().getUid().equals(Constants.UID)) return TYPE_SENT;
        return TYPE_RECEIVED;
    }


    public class SentMessagesViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView timeSent;

        public SentMessagesViewHolder(View view){
            super(view);
            content = view.findViewById(R.id.text_message_body);
            timeSent = view.findViewById(R.id.text_message_time);
        }
    }

    public class RecievedMessagesViewHolder extends SentMessagesViewHolder{
        TextView user;
        CircleImageView profileImg;

        public RecievedMessagesViewHolder(View view){
            super(view);
            profileImg = view.findViewById(R.id.image_message_profile);
            user = view.findViewById(R.id.text_message_name);
        }
    }

}
