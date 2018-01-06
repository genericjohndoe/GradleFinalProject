package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joeljohnson on 12/13/17.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    Context context;
    List<Comment> comments;


    public CommentsAdapter(Context context, List<Comment> objects) {
        this.context = context;
        this.comments = objects;
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileImg;
        TextView userNameContent;
        TextView timeDate;

        public CommentsViewHolder(View view){
            super(view);
            profileImg = view.findViewById(R.id.profile_imageview);
            userNameContent = view.findViewById(R.id.user_name_textView);
            timeDate = view.findViewById(R.id.time_date_textView);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_cell, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        final Comment comment = comments.get(position);

        Glide.with(context).load(comment.getProfilePictureURL()).into(holder.profileImg);
        holder.userNameContent.setText(comment.getUserName() + " " + comment.getCommentContent());
        holder.timeDate.setText(comment.getTimeDate());
    }
}
