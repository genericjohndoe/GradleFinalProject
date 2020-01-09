package com.udacity.gradle.builditbigger.comments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.databinding.CommentCellBinding;
import com.udacity.gradle.builditbigger.jokes.ViewHolderViewModel;
import com.udacity.gradle.builditbigger.mainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.models.Comment;

import java.util.List;


/**
 * styles comments for recyclerview
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    private Context context;
    private List<Comment> comments;


    public CommentsAdapter(Context context, List<Comment> objects) {
        this.context = context;
        this.comments = objects;
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner{
        private CommentCellBinding bind;
        private LifecycleRegistry mLifecycleRegistry;

        public CommentsViewHolder(CommentCellBinding bind){
            super(bind.getRoot());
            this.bind = bind;
            mLifecycleRegistry = new LifecycleRegistry(this);
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return mLifecycleRegistry;
        }

        public LifecycleRegistry getmLifecycleRegistry() {
            return mLifecycleRegistry;
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
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_START);
        ViewHolderViewModel viewHolderViewModel = new ViewHolderViewModel(comment);
        viewHolderViewModel.getUserNameLiveData().observe(holder, name ->{
            String content = name + " " + comment.getCommentContent();
            holder.bind.userNameTextView.setText(content);
        });
        viewHolderViewModel.getProfileImgLiveData().observe(holder, url ->{
            Glide.with(context).load(url).into(holder.bind.profileImageview);
        });

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
        holder.bind.timeDateTextView.setText(Constants.formattedTimeString(context, comment.getTimeDate(), false));
        holder.bind.deleteTextView.setOnClickListener(view ->{
            Constants.DATABASE
                    .child("userpostslikescomments/"+comment.getPostUid()+"/"+comment.getPostPushId()
                            +"/comments/commentlist/"+comment.getCommentId()).removeValue();
            if (comments.remove(comment)) notifyDataSetChanged();
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CommentsViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CommentsViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    public void onViewRecycled(@NonNull CommentsViewHolder holder) {
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onViewRecycled(holder);
    }
}
