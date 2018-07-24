package com.udacity.gradle.builditbigger.Forums.Replies;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.functions.FirebaseFunctions;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Forums.ForumQuestionsViewModel;
import com.udacity.gradle.builditbigger.Models.ForumQuestion;
import com.udacity.gradle.builditbigger.Models.ForumReply;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentForumQuestionBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumQuestionFragment extends Fragment {
    private String forumKey;
    private String authorUID;

    public ForumQuestionFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForumQuestionFragment.
     */
    public static ForumQuestionFragment newInstance(String key) {
        ForumQuestionFragment fragment = new ForumQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) forumKey = getArguments().getString("key");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentForumQuestionBinding bind = DataBindingUtil.inflate(inflater, R.layout.fragment_forum_question, container, false);

        List<ForumReply> replies = new ArrayList<>();
        ForumReplyAdapter adapter = new ForumReplyAdapter(replies, getActivity(), forumKey);
        bind.recyclerview.setAdapter(adapter);
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        bind.addButton.setOnClickListener(view -> {
            String contents = bind.answerEditText.getText().toString();
            DatabaseReference db = Constants.DATABASE.child("forumquestionreplies/" + forumKey).push();
            ForumReply forumReply = new ForumReply(Constants.USER, contents, System.currentTimeMillis(), db.getKey());
            db.setValue(forumReply, (databaseError, databaseReference) -> {
                if (databaseError == null) {
                    InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(bind.answerEditText.getWindowToken(), 0);
                    bind.answerEditText.setText("");
                    Map<String, Object> data = new HashMap<>();
                    data.put("forumQuestionKey", forumKey);
                    data.put("uid", authorUID);
                    data.put("replierUserName", Constants.USER.getUserName());
                    data.put("contents", contents);
                    FirebaseFunctions.getInstance().getHttpsCallable("onForumReply").call(data);
                    if (bind.answerEditText.getMentions().size() > 0) {
                        data.put("userNameList", bind.answerEditText.getMentions());
                        FirebaseFunctions.getInstance().getHttpsCallable("onReplyMentionCreated").call(data);
                    }
                }
            });
        });

        ForumQuestionsViewModel forumQuestionsViewModel = ViewModelProviders.of(this).get(ForumQuestionsViewModel.class);
        forumQuestionsViewModel.getForumQuestionLiveData(forumKey).observe(this, forumQuestion -> {
            String userName = "@" + forumQuestion.getHilarityUser().getUserName();
            bind.question.userNameTextView.setText(userName);
            bind.question.questionTextView.setText(forumQuestion.getQuestion());
            bind.question.questionTextView.requestFocus();
            bind.question.timeTextView.setText(Constants.formattedTimeString(getActivity(), forumQuestion.getTimeStamp()));
            authorUID = forumQuestion.getHilarityUser().getUid();
        });
        forumQuestionsViewModel.getForumQuestionReplyLiveData(forumKey).observe(this, reply -> {
            if (!replies.contains(reply)) {
                replies.add(reply);
                adapter.notifyDataSetChanged();
            }
        });
        return bind.getRoot();
    }

}
