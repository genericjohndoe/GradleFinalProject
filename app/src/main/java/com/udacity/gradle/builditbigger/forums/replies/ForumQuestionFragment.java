package com.udacity.gradle.builditbigger.forums.replies;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.functions.FirebaseFunctions;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.databinding.FragmentForumQuestionBinding;
import com.udacity.gradle.builditbigger.forums.ForumQuestionsViewModel;
import com.udacity.gradle.builditbigger.forums.createQuestion.NewQuestionActivity;
import com.udacity.gradle.builditbigger.mainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.models.ForumReply;

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
    private ForumQuestionsViewModel forumQuestionsViewModel;
    private FragmentForumQuestionBinding bind;

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
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_forum_question, container, false);

        List<ForumReply> replies = new ArrayList<>();
        ForumReplyAdapter adapter = new ForumReplyAdapter(replies, getActivity(), forumKey);
        bind.recyclerview.setAdapter(adapter);
        bind.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        bind.addButton.setOnClickListener(view -> {
            String contents = bind.answerEditText.getText().toString().trim();
            if (contents.length() > 0) {
                DatabaseReference db = Constants.DATABASE.child("forumquestionreplies/" + forumKey).push();
                ForumReply forumReply = new ForumReply(Constants.UID, contents, System.currentTimeMillis(), db.getKey());
                db.setValue(forumReply, (databaseError, databaseReference) -> {
                    InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(bind.answerEditText.getWindowToken(), 0);
                    bind.answerEditText.setText("");
                    if (databaseError == null && !Constants.UID.equals(authorUID)) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("forumQuestionKey", forumKey);
                        data.put("uid", authorUID);
                        data.put("replierUserName", Constants.USER.getUserName());
                        data.put("contents", contents);
                        FirebaseFunctions.getInstance().getHttpsCallable("onForumReply").call(data);
                        Log.i("mentions", "" + bind.answerEditText.getMentions().size());
                        if (bind.answerEditText.getMentions().size() > 0) {
                            Log.i("userNameList", bind.answerEditText.getMentions().toString());
                            data.put("userNameList", bind.answerEditText.getMentions());
                            FirebaseFunctions.getInstance().getHttpsCallable("onReplyMentionCreated").call(data);
                        }
                    }
                });
            }
        });

        forumQuestionsViewModel = ViewModelProviders.of(this).get(ForumQuestionsViewModel.class);
        forumQuestionsViewModel.getForumQuestionLiveData(forumKey).observe(this, forumQuestion -> {
            if (forumQuestion != null) {
                showName(forumQuestion.getHilarityUserUID());
                if (Constants.UID.equals(forumQuestion.getHilarityUserUID())) {
                    bind.question.deleteImageButton.setVisibility(View.VISIBLE);
                    bind.question.editImageButton.setVisibility(View.VISIBLE);
                }
                bind.question.questionTextView.setText(forumQuestion.getQuestion());
                bind.question.questionTextView.requestFocus();
                bind.question.timeTextView.setText(Constants.formattedTimeString(getActivity(), forumQuestion.getTimeStamp(), false));
                authorUID = forumQuestion.getHilarityUserUID();
            }
        });
        forumQuestionsViewModel.getForumQuestionReplyLiveData(forumKey).observe(this, reply -> {
            if (!replies.contains(reply)) {
                replies.add(reply);
                adapter.notifyDataSetChanged();
            }
        });
        bind.question.deleteImageButton.setOnClickListener(view -> {
            Constants.DATABASE.child("forumquestions/"+forumKey).removeValue((databaseError, databaseReference) -> {
                if (databaseError == null){
                    Constants.DATABASE.child("forumquestionreplies/"+forumKey).removeValue();
                    Intent intent = new Intent(getActivity(), HilarityActivity.class);
                    intent.putExtra("number",5);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        });
        bind.question.editImageButton.setOnClickListener(view ->{
            Intent intent = new Intent(getActivity(), NewQuestionActivity.class);
            intent.putExtra("contents", bind.question.questionTextView.getText().toString().trim());
            intent.putExtra("key", forumKey);
            startActivity(intent);
        });
        return bind.getRoot();
    }

    public void showName(String uid) {
        forumQuestionsViewModel.getUserNameLiveData(uid).observe(this, name -> {
            String userName = "@" + name;
            bind.question.userNameTextView.setText(userName);
        });
    }

}
