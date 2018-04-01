package com.udacity.gradle.builditbigger.Forums.Replies;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Forums.ForumQuestionsViewModel;
import com.udacity.gradle.builditbigger.Models.ForumQuestion;
import com.udacity.gradle.builditbigger.Models.ForumReply;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentForumQuestionBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumQuestionFragment extends Fragment {
    private ForumQuestion forumQuestion;

    public ForumQuestionFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ForumQuestionFragment.
     */
    public static ForumQuestionFragment newInstance(ForumQuestion forumQuestion) {
        ForumQuestionFragment fragment = new ForumQuestionFragment();
        fragment.forumQuestion = forumQuestion;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentForumQuestionBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_forum_question, container, false);
        bind.question.userNameTextView.setText("@"+forumQuestion.getHilarityUser().getUserName());
        bind.question.questionTextView.setText(forumQuestion.getQuestion());
        bind.question.timeTextView.setText(Constants.formattedTimeString(getActivity(),forumQuestion.getTimeStamp()));
        List<ForumReply> replies = new ArrayList<>();
        ForumReplyAdapter adapter = new ForumReplyAdapter(replies, getActivity(), forumQuestion.getKey());
        bind.recyclerview.setAdapter(adapter);
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        bind.addButton.setOnClickListener(view -> {
            String contents = bind.answerEditText.getText().toString();
            DatabaseReference db = Constants.DATABASE.child("forumquestions/"+forumQuestion.getKey()).push();
            ForumReply forumReply = new ForumReply(Constants.USER, contents, System.currentTimeMillis(), db.getKey());
            db.setValue(forumReply);
        });
        ViewModelProviders.of(this).get(ForumQuestionsViewModel.class)
                .getForumQuestionReplyLiveData(forumQuestion.getKey()).observe(this, reply ->{
            if (!replies.contains(reply)){
                replies.add(reply);
                adapter.notifyDataSetChanged();
            }
        });
        return bind.getRoot();
    }

}
