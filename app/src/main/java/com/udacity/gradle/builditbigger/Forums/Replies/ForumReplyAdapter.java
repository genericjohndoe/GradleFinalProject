package com.udacity.gradle.builditbigger.Forums.Replies;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Forums.Questions.ForumQuestionAdapter;
import com.udacity.gradle.builditbigger.Jokes.UserNameLiveData;
import com.udacity.gradle.builditbigger.Jokes.ViewHolderViewModel;
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

    public ForumReplyAdapter(List<ForumReply> forumReplies, Context context, String key) {
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
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_START);
        ForumReply forumReply = forumReplies.get(position);
        holder.forumReply = forumReply;
        holder.bind.replyTextView.setText(forumReply.getContent());
        ViewHolderViewModel viewHolderViewModel = new ViewHolderViewModel(forumReply);
        viewHolderViewModel.getUserNameLiveData().observe(holder, name ->{
            String userName = "@"+name;
            holder.bind.userNameTextView.setText(userName);
        });

        holder.bind.userNameTextView.setOnMentionClickListener((socialView, s) -> {
            Constants.DATABASE.child("inverseuserslist/" + s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Intent intent = new Intent(context, HilarityActivity.class);
                    intent.putExtra("uid", dataSnapshot.getValue(String.class));
                    intent.putExtra("number", 4);
                    context.startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            return null;
        });
        holder.bind.timeTextView.setText(Constants.formattedTimeString(context, forumReply.getTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return forumReplies.size();
    }

    public class ForumReplyViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner {
        CellForumReplyBinding bind;
        ForumReply forumReply;
        private LifecycleRegistry mLifecycleRegistry;

        public ForumReplyViewHolder(CellForumReplyBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
            mLifecycleRegistry = new LifecycleRegistry(this);
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
            bind.deleteButton.setOnClickListener(view -> {
                if (forumReply.getHilarityUserUID().equals(Constants.UID)) {
                    Constants.DATABASE.child("forumquestionreplies/" + key + "/" + forumReply.getKey())
                            .removeValue((databaseError, databaseReference) -> {
                                if (databaseError == null) {
                                    forumReplies.remove(forumReply);
                                    notifyDataSetChanged();
                                }
                            });
                }
            });
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
        public void onViewAttachedToWindow(@NonNull ForumReplyViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull ForumReplyViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        }

        @Override
        public void onViewRecycled(@NonNull ForumReplyViewHolder holder) {
            holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
            super.onViewRecycled(holder);
        }

}
