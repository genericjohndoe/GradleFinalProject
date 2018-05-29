package com.udacity.gradle.builditbigger.Messaging.Transcripts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messages;
    private Context context;
    private static final int TYPE_SENT = 0;
    private static final int TYPE_RECEIVED = 1;
    private static final int TYPE_SENT_IMAGE = 2;
    private static final int TYPE_RECEIVED_IMAGE = 3;

    public MessagesAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_SENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message_layout, parent, false);
                return new SentMessagesViewHolder(view);
            case TYPE_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recieved_message_layout, parent, false);
                return new RecievedMessagesViewHolder(view);
            case TYPE_SENT_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_image_messge_layout, parent, false);
                return new SentImageMessagesViewholder(view);
            case TYPE_RECEIVED_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_image_message_layout, parent, false);
                return new RecievedImageMessagesViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder instanceof SentMessagesViewHolder) {
            if (message.getContents().size() == 1)
                ((SentMessagesViewHolder) holder).content.setText(message.getContents().get(0));
            ((SentMessagesViewHolder) holder).timeSent.setText(Constants.formattedTimeString(context, message.getTimeStamp()));
            if (holder instanceof RecievedMessagesViewHolder) {
                ((RecievedMessagesViewHolder) holder).user.setText(message.getHilarityUser().getUserName());
                Glide.with(context).load(message.getHilarityUser().getUrlString())
                        .into(((RecievedMessagesViewHolder) holder).profileImg);
            }
        } else {
            ((SentImageMessagesViewholder) holder).timeSent.setText(Constants.formattedTimeString(context, message.getTimeStamp()));
            Constants.STORAGE.child(message.getContents().get(0)).getDownloadUrl().addOnSuccessListener(uri ->{
                Glide.with(context).load(uri).into(((SentImageMessagesViewholder) holder).imageView);
            });
            /*Glide.with(context).load(message.getContents().get(1))
                    .into(((SentImageMessagesViewholder) holder).imageView);*/
            if (holder instanceof  RecievedImageMessagesViewHolder){
                ((RecievedImageMessagesViewHolder) holder).user.setText(message.getHilarityUser().getUserName());
                Glide.with(context).load(message.getHilarityUser().getUrlString())
                        .into(((RecievedImageMessagesViewHolder) holder).profileImg);
            }
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getContents().size() == 1) {
            if (messages.get(position).getHilarityUser().getUid().equals(Constants.UID))
                return TYPE_SENT;
            return TYPE_RECEIVED;
        } else {
            if (messages.get(position).getHilarityUser().getUid().equals(Constants.UID))
                return TYPE_SENT_IMAGE;
            return TYPE_RECEIVED_IMAGE;
        }
    }


    public class SentMessagesViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView timeSent;

        public SentMessagesViewHolder(View view) {
            super(view);
            content = view.findViewById(R.id.tmessage_imageView);
            timeSent = view.findViewById(R.id.text_message_time);
        }
    }

    public class RecievedMessagesViewHolder extends SentMessagesViewHolder {
        TextView user;
        CircleImageView profileImg;

        public RecievedMessagesViewHolder(View view) {
            super(view);
            profileImg = view.findViewById(R.id.image_message_profile);
            user = view.findViewById(R.id.text_message_name);
        }
    }

    public class SentImageMessagesViewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView timeSent;

        public SentImageMessagesViewholder(View view) {
            super(view);
            imageView = view.findViewById(R.id.message_imageView);
            timeSent = view.findViewById(R.id.text_message_time);
        }
    }

    public class RecievedImageMessagesViewHolder extends SentImageMessagesViewholder {
        TextView user;
        CircleImageView profileImg;

        public RecievedImageMessagesViewHolder(View view) {
            super(view);
            profileImg = view.findViewById(R.id.image_message_profile);
            user = view.findViewById(R.id.text_message_name);
        }
    }

}
