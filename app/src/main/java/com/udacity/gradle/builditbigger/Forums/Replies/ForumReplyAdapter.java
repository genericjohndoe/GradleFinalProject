package com.udacity.gradle.builditbigger.Forums.Replies;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.ForumReply;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.CellForumReplyBinding;

import java.util.List;

/**
 * Created by joeljohnson on 3/31/18.
 */

public class ForumReplyAdapter extends RecyclerView.Adapter<ForumReplyAdapter.ForumReplyViewHolder> {
    private List<ForumReply> forumReplies;
    private Context context;
    private String key;

    public ForumReplyAdapter(List<ForumReply> forumReplies, Context context, String key){
        this.forumReplies = forumReplies;
        this.context = context;
        this.key = key;
    }

    @NonNull
    @Override
    public ForumReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CellForumReplyBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cell_forum_reply, parent, false);
        return new ForumReplyViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumReplyViewHolder holder, int position) {
        ForumReply forumReply = forumReplies.get(position);
        holder.forumReply = forumReply;
        holder.bind.replyTextView.setText(forumReply.getContent());
        holder.bind.userNameTextView.setText("@"+forumReply.getHilarityUser().getUserName());
        holder.bind.userNameTextView.setOnMentionClickListener((socialView, s) -> {
            Constants.DATABASE.child("inverseuserslist/" + s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Intent intent = new Intent(context, HilarityActivity.class);
                    intent.putExtra("uid", dataSnapshot.getValue(String.class));
                    intent.putExtra("number", 4);
                    context.startActivity(intent);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
            return null;
        });
        holder.bind.timeTextView.setText(Constants.formattedTimeString(context, forumReply.getTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return forumReplies.size();
    }

    public class ForumReplyViewHolder extends RecyclerView.ViewHolder {
        CellForumReplyBinding bind;
        ForumReply forumReply;

        public ForumReplyViewHolder(CellForumReplyBinding bind){
            super(bind.getRoot());
            this.bind = bind;
            bind.editButton.setOnClickListener(view -> {
                //todo send to activity that allows for editting
            });
            bind.deleteButton.setOnClickListener(view -> {
                Constants.DATABASE.child("forumreplies/"+key+"/"+forumReply.getKey()).removeValue((databaseError, databaseReference) -> {
                    forumReplies.remove(forumReply);
                    notifyDataSetChanged();
                });
            });
        }
    }
}
