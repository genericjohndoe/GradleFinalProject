package com.udacity.gradle.builditbigger.Forums.Questions;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Forums.Replies.ForumQuestionActivity;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.ForumQuestion;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.CellForumQuestionBinding;

import java.util.List;

/**
 * Created by joeljohnson on 3/31/18.
 */

public class ForumQuestionAdapter extends RecyclerView.Adapter<ForumQuestionAdapter.ForumQuestionViewHolder> {

    List<ForumQuestion> forumQuestions;
    Context context;

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
        ForumQuestion forumQuestion = forumQuestions.get(position);
        holder.forumQuestion = forumQuestion;
        holder.bind.questionTextView.setText(forumQuestion.getQuestion());
        String userName = "@"+forumQuestion.getHilarityUser().getUserName();
        holder.bind.userNameTextView.setText(userName);
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
        holder.bind.timeTextView.setText(Constants.formattedTimeString(context,forumQuestion.getTimeStamp()));
    }

    @Override
    public int getItemCount() {
        return forumQuestions.size();
    }

    public class ForumQuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CellForumQuestionBinding bind;
        ForumQuestion forumQuestion;

        public ForumQuestionViewHolder(CellForumQuestionBinding bind){
            super(bind.getRoot());
            this.bind = bind;
            bind.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ForumQuestionActivity.class);
            intent.putExtra("question", forumQuestion);
            context.startActivity(intent);
        }
    }
}
