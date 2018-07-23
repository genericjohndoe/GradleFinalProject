package com.udacity.gradle.builditbigger.Comments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.Comment;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.CommentCellBinding;

import java.util.List;


/**
 * styles comments for recyclerview
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    Context context;
    List<Comment> comments;


    public CommentsAdapter(Context context, List<Comment> objects) {
        this.context = context;
        this.comments = objects;
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        CommentCellBinding bind;
        public CommentsViewHolder(CommentCellBinding bind){
            super(bind.getRoot());
            this.bind = bind;
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentCellBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.comment_cell, parent, false);
        return new CommentsViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        final Comment comment = comments.get(position);

        Glide.with(context).load(comment.getHilarityUser().getUrlString()).into(holder.bind.profileImageview);
        String content = comment.getHilarityUser().getUserName() + " " + comment.getCommentContent();
        holder.bind.userNameTextView.setText(content);
        holder.bind.userNameTextView.setOnMentionClickListener((socialView, s) -> {
            Constants.DATABASE.child("inverseuserslist/"+s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Intent intent = new Intent(context, HilarityActivity.class);
                        intent.putExtra("number", 4);
                        intent.putExtra("uid", dataSnapshot.getValue(String.class));
                        context.startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
            return null;
        });
        holder.bind.timeDateTextView.setText(Constants.formattedTimeString(context, comment.getTimeDate()));
        holder.bind.deleteTextView.setOnClickListener(view ->{
            Constants.DATABASE
                    .child("userpostslikescomments/"+comment.getPostUid()+"/"+comment.getPostPushId()
                            +"/comments/commentlist/"+comment.getCommentId()).removeValue();
            if (comments.remove(comment)) notifyDataSetChanged();
        });
    }
}
