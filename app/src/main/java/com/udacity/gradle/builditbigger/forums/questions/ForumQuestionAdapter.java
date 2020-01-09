package com.udacity.gradle.builditbigger.forums.questions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.databinding.CellForumQuestionBinding;
import com.udacity.gradle.builditbigger.forums.createQuestion.NewQuestionActivity;
import com.udacity.gradle.builditbigger.forums.replies.ForumQuestionActivity;
import com.udacity.gradle.builditbigger.jokes.ViewHolderViewModel;
import com.udacity.gradle.builditbigger.mainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.models.ForumQuestion;

import java.util.List;

/**
 * Created by joeljohnson on 3/31/18.
 */

public class ForumQuestionAdapter extends RecyclerView.Adapter<ForumQuestionAdapter.ForumQuestionViewHolder> {

    private List<ForumQuestion> forumQuestions;
    private Context context;

    public ForumQuestionAdapter(List<ForumQuestion> forumQuestions, Context context){
        this.forumQuestions = forumQuestions;
        this.context = context;
    }

    @NonNull
    @Override
    public ForumQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CellForumQuestionBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cell_forum_question, parent, false);
        return new ForumQuestionViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumQuestionViewHolder holder, int position) {
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_START);
        ForumQuestion forumQuestion = forumQuestions.get(position);
        holder.forumQuestion = forumQuestion;
        if (Constants.UID.equals(forumQuestion.getHilarityUserUID())) {
            holder.bind.deleteImageButton.setVisibility(View.VISIBLE);
            holder.bind.editImageButton.setVisibility(View.VISIBLE);
        }
        holder.bind.questionTextView.setText(forumQuestion.getQuestion());
        ViewHolderViewModel viewHolderViewModel = new ViewHolderViewModel(forumQuestion);
        viewHolderViewModel.getUserNameLiveData().observe(holder, name -> {
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
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
            return null;
        });
        holder.bind.timeTextView.setText(Constants.formattedTimeString(context,forumQuestion.getTimeStamp(), false));
    }

    @Override
    public int getItemCount() {
        return forumQuestions.size();
    }

    public class ForumQuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, LifecycleOwner{
        private CellForumQuestionBinding bind;
        private ForumQuestion forumQuestion;
        private LifecycleRegistry mLifecycleRegistry;

        public ForumQuestionViewHolder(CellForumQuestionBinding bind){
            super(bind.getRoot());
            this.bind = bind;
            bind.deleteImageButton.setOnClickListener(view -> {
                Constants.DATABASE.child("forumquestions/"+forumQuestion.getKey()).removeValue((databaseError, databaseReference) -> {
                    if (databaseError == null){
                        forumQuestions.remove(forumQuestion);
                        notifyDataSetChanged();
                        Constants.DATABASE.child("forumquestionreplies/"+forumQuestion.getKey()).removeValue();
                    }
                });
            });
            bind.editImageButton.setOnClickListener(view ->{
                Intent intent = new Intent(context, NewQuestionActivity.class);
                intent.putExtra("contents", forumQuestion.getQuestion());
                intent.putExtra("key", forumQuestion.getKey());
                context.startActivity(intent);
            });
            bind.getRoot().setOnClickListener(this);
            mLifecycleRegistry = new LifecycleRegistry(this);
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ForumQuestionActivity.class);
            intent.putExtra("key", forumQuestion.getKey());
            context.startActivity(intent);
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
    public void onViewAttachedToWindow(@NonNull ForumQuestionViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ForumQuestionViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    public void onViewRecycled(@NonNull ForumQuestionViewHolder holder) {
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onViewRecycled(holder);
    }
}
